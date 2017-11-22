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
			question.setContent("1.Met Opera的官网首先是需要注册，但是我在美国并没有固定手机号，前几次去美国都是临时在某宝买的两个月的号（用完就扔那种），但是我担心如果我在提前买票的时候（尤其是注册时）如果需要手机短信确认一类就很不好办了，尽管官网也支持把国家选为China，但是这样就不能选省份了，另外我也不知道他们是把票寄到特定地址还是什么其他方式，所以我很担心这些事出差错。再者我也不知道Met Opera支持哪些支付方式\n" +
					"【更新：支付的问题我已经搞定啦！蟹蟹大家帮忙】");

			question.setTitle("在纽约大都会歌剧院观看歌剧需要做哪些准备工作？");
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
