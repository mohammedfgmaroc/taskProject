package com.tms.tmsustem.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tms.tmsustem.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

	// Inside TaskRepository interface
	List<Task> findByUserId(Long userId);
	
	List<Task> findByUserIdAndPriority(Long userId, Task.Priority priority);

    List<Task> findByUserIdAndStatus(Long userId, Task.Status status);
    
    //search by keyWord
    
    List<Task> findByUserIdAndTitleContainingOrDescriptionContainingIgnoreCase(
            Long userId, String titleKeyword, String descriptionKeyword);
    
}
