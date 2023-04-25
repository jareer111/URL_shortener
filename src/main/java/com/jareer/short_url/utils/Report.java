package com.jareer.short_url.utils;

import com.jareer.short_url.config.security.SessionUser;
import com.jareer.short_url.config.security.UserDetails;
import com.jareer.short_url.dto.report.DailyReportDTO;
import com.jareer.short_url.dto.report.UrlReport;
import com.jareer.short_url.dto.report.WeeklyReportDTO;
import com.jareer.short_url.services.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Report {
    private final UrlService urlService;
    private final SessionUser sessionUser;
    private final MailSenderService mailSenderService;

    public WeeklyReportDTO report() {
        UserDetails user = sessionUser.user();
        AtomicInteger counter = new AtomicInteger(0);
        List<DailyReportDTO> dailyReportDTOS = new ArrayList<>();

        urlService.lastWeek()
                .stream().map(url -> {
                    counter.incrementAndGet();
                    return new UrlReport(url);
                })
                .collect(Collectors.groupingBy(urlReport -> urlReport.getDayOfWeek().getValue()))
                .forEach((k, v) -> {
                    DailyReportDTO dailyReportDTO = new DailyReportDTO(v, DayOfWeek.of(k));
                    dailyReportDTOS.add(dailyReportDTO);
                });
        WeeklyReportDTO weeklyReportDTO = new WeeklyReportDTO(dailyReportDTOS, LocalDateTime.now(), LocalDateTime.now(), counter.get());

        Map<String, Object> model = Map.of(
                "to", user.getEmail(),
                "subject", "Weekly Report",
                "weeklyReport", weeklyReportDTO);
        mailSenderService.report(model);
        return weeklyReportDTO;
    }
}
