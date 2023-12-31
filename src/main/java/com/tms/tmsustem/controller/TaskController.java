package com.tms.tmsustem.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tms.tmsustem.dto.UserDto;
import com.tms.tmsustem.model.Task;
import com.tms.tmsustem.model.User;
import com.tms.tmsustem.repository.TaskRepository;
import com.tms.tmsustem.service.NotificationService;
import com.tms.tmsustem.service.PdfExportService;
import com.tms.tmsustem.service.TaskService;
import com.tms.tmsustem.service.UserService;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;



@Controller
public class TaskController {

	@Autowired
    private TaskRepository taskRepository;
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
    private PdfExportService pdfExportService;
	
	@Autowired
    private TaskService taskService;
	
	@Autowired
    private NotificationService notificationService;
	
	@GetMapping("/registration")
	public String getRegistrationPage(@ModelAttribute("user") UserDto userDto) {
		return "register";
	}
	
	@PostMapping("/registration")
	public String saveUser(@ModelAttribute("user") UserDto userDto, RedirectAttributes redirectAttributes) {
		userService.save(userDto);
		redirectAttributes.addFlashAttribute("successMessage", "Registration Successful! Please log in.");
		return "redirect:/login";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/tasks")
	public String listTasks(Model model,
	        Principal principal,
	        @RequestParam(required = false) Task.Priority priority,
	        @RequestParam(required = false) String category,
	        @RequestParam(required = false) Task.Status status,
	        @RequestParam(required = false) String search) {
		
	    
		
		if (principal != null) {
		    UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		    
		 // Retrieve tasks based on the authenticated user, filter parameters, and search keyword
	        List<Task> tasks = taskService.findTasksWithFiltersAndSearch(principal.getName(), priority, status, search);
	        // Add tasks to the model
	        model.addAttribute("tasks", tasks);
	     // Retrieve notifications and add them to the model
	        List<String> notifications = notificationService.getPendingNotifications();
	        model.addAttribute("notifications", notifications);

	        // Clear notifications after displaying them
	        notificationService.clearNotifications();
	        
		    model.addAttribute("user", userDetails);
		 // Add dropdown options for priority, category, and status
		    model.addAttribute("priorities", Task.Priority.values());
		    model.addAttribute("statuses", Task.Status.values());
		    model.addAttribute("searchKeyword", search);
		}

	    // Return the name of the view template
	    return "task/list";
	}

    @GetMapping("/tasks/create")
    public String createTaskForm(Model model, Principal principal) {
    	if (principal != null) {
    	UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
    	model.addAttribute("user", userDetails);
    	}
        model.addAttribute("task", new Task());
        
        return "task/create";
    }

    @PostMapping("/tasks/create")
    public String createTask(@ModelAttribute Task task, Principal principal) {
    	if (principal != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            User authenticatedUser = userService.findByUsername(userDetails.getUsername());
            task.setUser(authenticatedUser); // Set the authenticated user for the task
            taskRepository.save(task);
        }
        return "redirect:/tasks";
    }

    @GetMapping("/tasks/{id}/edit")
    public String editTaskForm(@PathVariable Long id, Model model, Principal principal) {
    	if (principal != null) {
        	UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        	model.addAttribute("user", userDetails);
        	}
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task id: " + id));

        model.addAttribute("task", task);
        return "task/edit";
    }

    @PostMapping("/tasks/{id}/edit")
    public String editTask(@PathVariable Long id, @ModelAttribute Task updatedTask) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task id: " + id));

        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setDueDate(updatedTask.getDueDate());
        task.setPriority(updatedTask.getPriority());
        task.setCategory(updatedTask.getCategory());
        task.setStatus(updatedTask.getStatus());

        taskRepository.save(task);
        return "redirect:/tasks";
    }

    @GetMapping("/tasks/{id}/delete")
    public String deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
        return "redirect:/tasks";
    }
    
    @GetMapping("/export/pdf")
    public void exportTasksToPdf(HttpServletResponse response, Principal principal) {
        // Retrieve tasks based on the authenticated user
        List<Task> tasks = taskService.findTasksByUsername(principal.getName());

        // Set the response headers
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=tasks_report.pdf");

        try (ServletOutputStream outputStream = response.getOutputStream()) {
            // Export tasks to PDF and write to the response stream
            pdfExportService.exportTasksToPdf(tasks, outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
