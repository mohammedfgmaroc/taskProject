package com.tms.tmsustem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tms.tmsustem.model.Task;
import com.tms.tmsustem.model.User;
import com.tms.tmsustem.repository.TaskRepository;
import com.tms.tmsustem.repository.UserRepository;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private UserRepository userRepository;

    // Add other methods as needed
    public List<Task> findTasksWithFilters(
            Long userId, Task.Priority priority, Task.Status status) {

        List<Task> tasks;

        if (priority != null) {
            tasks = taskRepository.findByUserIdAndPriority(userId, priority);
        } else if (status != null) {
            tasks = taskRepository.findByUserIdAndStatus(userId, status);
        } else {
            // No filters applied, return all tasks
            tasks = taskRepository.findByUserId(userId);
        }

        return tasks;
    }
    
    public List<Task> findTasksByUsername(String username) {
        // Assuming you have a findByUser method in your TaskRepository
        User user = userRepository.findByUsername(username);
        return taskRepository.findByUserId(user.getId());
    }
    
    public List<Task> findTasksWithFiltersAndSearch(
            String username, Task.Priority priority, Task.Status status, String searchKeyword) {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            // Handle the case where the user is not found (username is not valid)
            throw new RuntimeException("User not found for username: " + username);
        }

        Long userId = user.getId();
        List<Task> tasks;

        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            tasks = taskRepository.findByUserIdAndTitleContainingOrDescriptionContainingIgnoreCase(
                userId, searchKeyword, searchKeyword);
        } else {
            // If no search keyword, apply other filters using existing method
            tasks = findTasksWithFilters(userId, priority, status);
        }

        return tasks;
    }
}
