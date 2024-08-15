package com.example.server.timeline.controller;

import com.example.server.auth.util.SecurityContextHolderUtils;
import com.example.server.exception.BadRequestException;
import com.example.server.feed.dto.SocialPost;
import com.example.server.timeline.service.TimelineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/timeline")
@RequiredArgsConstructor
public class TimelineController {

    private final TimelineService timelineService;

    @GetMapping
    public List<SocialPost> getTimeline(@RequestParam(required = false) Integer lastSeenId,
                                            @RequestParam(defaultValue = "20") int limit) {
        if (limit > 30) {
            throw new BadRequestException();
        }
        int userId = SecurityContextHolderUtils.getUserId();
        return timelineService.getUserTimeline(userId, lastSeenId, limit);
    }
}
