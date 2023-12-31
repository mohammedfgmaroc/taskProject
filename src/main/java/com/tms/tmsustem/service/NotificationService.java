package com.tms.tmsustem.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tms.tmsustem.model.Task;
import com.tms.tmsustem.repository.TaskRepository;

@Service
public class NotificationService {

    @Autowired
    private TaskRepository taskRepository;

    private List<String> pendingNotifications = new ArrayList<>();

    @Scheduled(fixedRate = 60000) // Run every minute (adjust as needed)
    public void checkApproachingDueDates() {
    	// Calculate the start and end of the current day
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);

        // Retrieve tasks with approaching due dates for the logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<Task> tasksWithApproachingDueDates = taskRepository.findTasksWithApproachingDueDatesForUser(username, startOfDay, endOfDay);

        // Add notifications to the list
        for (Task task : tasksWithApproachingDueDates) {
            pendingNotifications.add("Task '" + task.getTitle() + "' is approaching its due date!");
        }
    }
    
    public List<String> getPendingNotifications() {
        return new ArrayList<>(pendingNotifications);
    }

    public void clearNotifications() {
        pendingNotifications.clear();
    }

    // Other methods...
}