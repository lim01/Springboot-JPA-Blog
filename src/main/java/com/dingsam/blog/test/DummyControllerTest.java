package com.dingsam.blog.test;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dingsam.blog.model.RoleType;
import com.dingsam.blog.model.Users;
import com.dingsam.blog.repository.UsersRepository;

@RestController
public class DummyControllerTest {
	
	@Autowired	// 의존성 주입
	private UsersRepository usersRepository;
	
	@GetMapping("/dummy/users/{id}")
	public Users detail(@PathVariable int id) {
		
//		Users user = usersRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
//			@Override
//			public IllegalArgumentException get() {
//				// TODO Auto-generated method stub
//				return new IllegalArgumentException("해당 유저는 없습니다. id:" + id);
//			}
//		});
		// 람다식
		Users user = usersRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("해당 유저는 없습니다. id:" + id);
		});
		
		return user;
	}
	
	@Transactional // 함수 종료시 자동 commit
	@PutMapping("/dummy/users/{id}")
	public Users updateUser(@PathVariable int id, @RequestBody Users requestUser) {
		
		System.out.println("id:" + id);
		System.out.println("password:" + requestUser.getPassword());
		System.out.println("email:" + requestUser.getEmail());
		// 람다식
		Users user = usersRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("수정에 실패하였습니다. id:" + id);
		});
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
		
		//usersRepository.save(user);
		
		// 더티 체킹
		return user;
	}
	
	@GetMapping("/dummy/users")
	public List<Users> list() {
		return usersRepository.findAll();
	}
	
	@GetMapping("/dummy/user")
	public List<Users> pageList(@PageableDefault(size=2, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
		Page<Users> pagiingUsers = usersRepository.findAll(pageable);
		
		List<Users> users = pagiingUsers.getContent();
		
		return users;
	}
	
	//http://localhost:8000/blog/dummy/join
	@PostMapping("/dummy/join")
	public String join(Users user) {
		System.out.println("username:" + user.getUsername());
		System.out.println("password:" + user.getPassword());
		System.out.println("email:" + user.getEmail());
		
		user.setRole(RoleType.USER);
		usersRepository.save(user);
		return "회원가입 완료";
	}
	
	@DeleteMapping("/dummy/users/{id}")
	public String delete(@PathVariable int id) {
		try {
			usersRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			return "회원 삭제 실패 id:" + id;
		}
		return "회원 삭제 완료 id:" + id;
	}
}
