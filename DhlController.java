package com.project.dhl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.dhl.constants.DhlConstants;
import com.project.dhl.dao.DhlTrackingInformationDao;
import com.project.dhl.service.DhlService;

@RestController
@RequestMapping("/dhlTracker")
public class DhlController {

	@Autowired
	DhlService dhlService;

	@PostMapping("/uploadCsv")
	public ResponseEntity<String> uploadCsvFile(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body("Please select a CSV file to upload.");
		}
		try {
			dhlService.processCSVFile(file);
			return ResponseEntity.ok("CSV file uploaded and processed successfully!");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Failed to upload and process the CSV file.");
		}
	}

	@GetMapping("/getStatus/{trackingId}")
	public ResponseEntity<?> getTrackingStatus(@PathVariable long trackingId) {
		String currentLocation = dhlService.getTrackingStatus(trackingId);
		if (currentLocation != null) {
			return ResponseEntity.ok(currentLocation);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tracking ID not found");
		}
	}

	@GetMapping("/getAllStatus")
	public List<DhlTrackingInformationDao> getAllTrackingStatus() {
		List<DhlTrackingInformationDao> trackingStatusList = dhlService.getAllStatus();
		return trackingStatusList;
	}
	
//	@GetMapping("/kafka")
//	public String kafkaProducer() {
//		kafkaTemplate.send("Shipment-Delivered-Details", "Hi MALINI!");
//		return "The message pushed to kafka!! Successfully";
//	}

}
