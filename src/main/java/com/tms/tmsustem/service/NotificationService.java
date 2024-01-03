package com.tms.tmsustem.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tms.tmsustem.model.Task;

@Service
public class NotificationService {

    public List<Task> getDueTasksForToday(List<Task> tasks) {
        // Obtenir la date actuelle
        LocalDate today = LocalDate.now();

        // Filtrer les tâches dont la date d'échéance est égale à la date d'aujourd'hui
        List<Task> dueTasksToday = tasks.stream()
                .filter(task -> {
                    LocalDate dueDate = LocalDate.parse(
                            task.getDueDate().toString(),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    );
                    return dueDate.isEqual(today);
                })
                .collect(Collectors.toList());

        // Retourner la liste des tâches correspondantes
        return dueTasksToday;
    }
}
