package edu.kh.project.common.scheduling.service;

import java.util.List;

public interface SchedulingService {

	/** db이미지 목록 조회
	 * @return
	 */
	List<String> selectImageList();

}
