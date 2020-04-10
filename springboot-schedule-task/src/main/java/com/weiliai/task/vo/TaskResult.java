package com.weiliai.task.vo;

import com.weiliai.task.constant.TaskConstant;
import lombok.*;

/**
 * 处理任务返回结果类
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskResult<T> {

	private String code;
	private String message;
	private T data;

	public static <T> TaskResult<T> ok(){
		return new TaskResult<T>(TaskConstant.SUCCESS,TaskConstant.SUCCESS_MSG,null);
	}

	public static <T> TaskResult<T> ok(T data){
		return new TaskResult<T>(TaskConstant.SUCCESS,TaskConstant.SUCCESS_MSG,data);
	}

	public static <T> TaskResult<T> fail(){
		return new TaskResult<T>(TaskConstant.FAIL,TaskConstant.FAIL_MSG,null);
	}

	public static <T> TaskResult<T> fail(T data){
		return new TaskResult<T>(TaskConstant.FAIL,TaskConstant.FAIL_MSG,data);
	}

}
