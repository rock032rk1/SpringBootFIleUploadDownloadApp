package com.junitmackitotest.app;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.junitmackitotest.app.entity.Document;
import com.junitmackitotest.app.repository.DocumentRepository;

//@SpringBootTest
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class SpringBootFileUploadExampleApplicationTests {

	@Autowired
	private DocumentRepository documentRepository;

	@Autowired
	private TestEntityManager testEntityManager;

	@Test
	@Rollback(false)
	void testInsertDocument() {
		File file = new File("D:\\w3schools.jpg");
		Document doc = new Document();
		doc.setName(file.getName());
		Document docuSaved = null;
		Document docuExist = null;
		try {
			
			byte[] bytes = Files.readAllBytes(file.toPath());
			doc.setContent(bytes);
			long fileSize=bytes.length;
			doc.setSize(fileSize);
			doc.setUploadTime(new Date());
			
			docuSaved = documentRepository.save(doc);
			
			docuExist=testEntityManager.find(Document.class, docuSaved.getId());
			
			assertThat(docuExist.getSize()).isEqualTo(fileSize);
			
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

}
