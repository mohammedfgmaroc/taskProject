package com.tms.tmsustem.model;

import java.time.LocalDateTime;

import java.time.LocalDate;

public class FullCalendarEvent {
    private String title;
    private LocalDateTime start;
    private LocalDateTime end;

    public FullCalendarEvent(String title, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.start = startDate.atStartOfDay();
        this.end = endDate.plusDays(1).atStartOfDay(); // Add one day to represent the end of the day
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public LocalDateTime getEnd() {
		return end;
	}

	public void setEnd(LocalDateTime end) {
		this.end = end;
	}

    // Getters and setters
    // ...
}

