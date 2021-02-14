package com.junitmackitotest.app.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.junitmackitotest.app.entity.Document;
import com.junitmackitotest.app.repository.DocumentRepository;



@Controller
public class FIleController {

	@Autowired
	private DocumentRepository documentRepository;
	
	@GetMapping("/")
	public String viewIndex(Model model) {
		
		List<Document> dlist=documentRepository.findAll();
		model.addAttribute("dlist", dlist);
		
		return "home";
	}
	
	@PostMapping("/uploadFile")
	public String uploadFile(@RequestParam("document") MultipartFile file,RedirectAttributes ra) throws IOException {
		
		String fileName=StringUtils.cleanPath(file.getOriginalFilename());
		
		Document doc=new Document();
		doc.setName(fileName);
		doc.setContent(file.getBytes());
		doc.setSize(file.getSize());
		doc.setUploadTime(new Date());
		
		documentRepository.save(doc);
		
		ra.addFlashAttribute("message", "The File save Successfully.......");
		
		return "redirect:/";
	}
	
	@GetMapping("/download")
	public void downloadFile(@RequestParam("id") Integer id,HttpServletResponse resp) throws Exception {
		
		Optional<Document> result= documentRepository.findById(id);
		
		if(!result.isPresent()) 
		{
			throw new Exception("Could not found Data With ID "+id);
		}
		
		Document doc=result.get();
		
		resp.setContentType("application/octet-stream");
		
		String headerKey="Content-Disposition";
		String headerValue="attachment; filename=" +doc.getName();
		
		resp.setHeader(headerKey, headerValue);
		
		ServletOutputStream outputStream = resp.getOutputStream();
		
		outputStream.write(doc.getContent());
		outputStream.close();
	}
}
