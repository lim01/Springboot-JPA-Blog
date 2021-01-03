package com.dingsam.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dingsam.blog.model.Users;

public interface UsersRepository extends JpaRepository<Users, Integer>	{

}
