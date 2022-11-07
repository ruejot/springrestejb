package org.wildfly.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wildfly.demo.service.impl.MOrderMTEARService;

@RestController
public class MOrderMTEARRestController {

	@Autowired
	private MOrderMTEARService mMTEARService;
	
	@GetMapping("/testEJBReloadMemoryCenter")
	public String testEJBReloadMemoryCenter(@RequestParam(name = "destination") final String dest) throws Exception {
		String testEJBReloadMemoryCenter_result = mMTEARService.getEJBReloadMemoryCenter(dest);
//		return "testEJBReloadMemoryCenter_Finished";
		return testEJBReloadMemoryCenter_result;
	}
	
	@GetMapping("/testEJBQueryMemoryCenter")
	public String testEJBQueryMemoryCenter(@RequestParam(name = "destination") final String dest) throws Exception {
		String testEJBQueryMemoryCenter_result = mMTEARService.getEJBQueryMemoryCenter(dest);
//		return "testEJBQueryMemoryCenter_Finished";
		return testEJBQueryMemoryCenter_result;
	}
//	@GetMapping("/getDataFromSysTable")
//	public String reloadSysTable(@RequestParam(name = "destination") final String destination,
//			@RequestParam(name = "key") final String key) throws Exception {
////		String result = mtRest.getSystableResult(destination, key);
//		return "not_finished_public String reloadSysTable";
//	}
	
	
	
}
