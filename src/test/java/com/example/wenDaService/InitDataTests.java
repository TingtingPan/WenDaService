package com.example.wenDaService;

import com.example.wenDaService.dao.QuestionDAO;
import com.example.wenDaService.dao.UserDAO;
import com.example.wenDaService.model.Question;
import com.example.wenDaService.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql("/init-schema.sql")
public class InitDataTests {

	@Autowired
	UserDAO userDao;

	@Autowired
	QuestionDAO questionDAO;

	@Test
	public void contextLoads() {
	}


	@Before
	public void initDatabase(){
		for (int i = 0;i<11;i++){
			User user = new User();
			Random random=new Random();
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
			user.setName(String.format("USER%d",i));
			user.setPassword("");
			user.setSalt("");
			//将用户信息保存到数据库
			userDao.addUser(user);
			List<User> users = userDao.selectAllUser();

			System.out.println("添加用户"+user.toString()+"后");
			System.out.println(users);

			//更新用户passwd
			user.setPassword("xxx");
			userDao.updatePassword(user);

			//Question
			Question question = new Question();
			question.setCommentCount(i);
			Date date = new Date();
			date.setTime(date.getTime()+1000*3600*i);
			question.setUserId(i+1);
			question.setCreateDate(date);
			question.setContent("sffeajshdsdf");
			question.setTitle("ptt");

			//将问题添加到数据库
			questionDAO.addQuestion(question);
		}



	}

	@Test
	public void userTest(){
		//检查用户库有多少数据
		List<User> users = userDao.selectAllUser();
		System.out.println("数据库中一共有"+users.size()+"条数据");
		//		List<Question>
		Assert.assertEquals("xxx",userDao.selectByid(1).getPassword());
		userDao.deleteById(1);
		Assert.assertNull(userDao.selectByid(1));

	}


	@Test
	public void testAllQuetion(){
		//		List<Question> questions = questionDAO.selectLatestQuestions(0,0,10);
		List<Question>questions= questionDAO.selectAllQuestions();
		System.out.println("数据库中一共有"+questions.size()+"条数据");
	}

	@Test
	public void selectLatestQuestions(){

		System.out.println(questionDAO.selectLatestQuestions(0,0,10).toString());
	}


}
