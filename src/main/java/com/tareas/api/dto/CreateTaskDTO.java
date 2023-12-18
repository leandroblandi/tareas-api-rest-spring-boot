package com.tareas.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateTaskDTO {

	@NotBlank
	private String taskTitle;

	@NotBlank
	private String taskDescription;

	private Long personId;

}
