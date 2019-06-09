package com.waes.web.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.waes.domain.Side;
import com.waes.model.DiffRequest;
import com.waes.model.DiffResponse;
import com.waes.service.DiffService;

/**
 * Declares endpoints used to save base64 
 * content for 2 sides and get the result comparison
 */
@RestController
@RequestMapping(value = "/diff", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class DiffController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DiffController.class);
	
	@Autowired
	private DiffService service;
	
	/**
     * Declares the endpoint to save the left side of base64 content.
     *
     * @param id: numeric identifier.
     * @param diffRequest: request object that contains the base64 encoded content.
     */
	@PostMapping(path = "/{id}/left", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void registerLeftSide(
    		@PathVariable(value="id") Long id,
    		@RequestBody DiffRequest diffRequest) {
		
		LOGGER.info(String.format("LEFT SIDE %s - %s", id, diffRequest.getContent()));
		
		service.save(id, diffRequest.getContent(), Side.LEFT);
    }
	
	/**
     * Declares the endpoint to save the right side of base64 content.
     *
     * @param id: numeric identifier.
     * @param diffRequest: request object that contains the base64 encoded content.
     */
	@PostMapping(path = "/{id}/right", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void registerRigthSide(
    		@PathVariable(value="id") Long id,
    		@RequestBody DiffRequest input) {
		
		LOGGER.info(String.format("RIGHT SIDE %s - %s", id, input.getContent()));
		
		service.save(id, input.getContent(), Side.RIGHT);

    }
	
	
	/**
     * Declares the endpoint to get the left and right result comparison of the same identifier.
     *
     * @param id: numeric identifier.
     * @return DiffResponse: response object that contains the result of comparison
     */
	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DiffResponse getDiffResult(
    		@PathVariable(value="id") Long id) {
		
		LOGGER.info(String.format("DIFF %s", id));

		return service.getResult(id);
		
    }
	
}
