package cn.pcm.serializable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Test;

public class Serializabletest {
	
	@Test
	public void serializableOutTest() {
		try {
			user u = new user("a", 2, "3");
			FileOutputStream fileOutput = new FileOutputStream("D://1.txt");
			ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
			objectOutput.writeObject(u);
			objectOutput.close();
			fileOutput.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void serializableInputtest() {
		try {
			FileInputStream fileInput = new FileInputStream("D://1.txt");
			ObjectInputStream objectInput = new ObjectInputStream(fileInput);
			user u = new user();
			u = (user) objectInput.readObject();
			System.out.println(u.getUsername() + "" + u.getAge() + ""
					+ u.getPassword());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
