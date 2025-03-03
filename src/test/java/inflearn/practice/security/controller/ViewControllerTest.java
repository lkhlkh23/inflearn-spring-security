package inflearn.practice.security.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import inflearn.practice.security.entity.AccountEntity;
import inflearn.practice.security.service.AccountService;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("spring security 의 filter chain 에 대한 테스트")
class ViewControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private AccountService accountService;

	@Test
	@WithAnonymousUser
	void test_index_anonymous() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/"))
			   .andDo(print())
			   .andExpect(status().isOk());
	}

	@Test
	@WithNormalUser
	void test_index_user() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/").with(user("doblee").roles("USER")))
			   .andDo(print())
			   .andExpect(status().isOk());
	}

	@Test
	void test_index_admin() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/").with(user("doblee").roles("ADMIN")))
			   .andDo(print())
			   .andExpect(status().isOk());
	}

	@Test
	void test_admin_user() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/admin").with(user("doblee").roles("USER")))
			   .andDo(print())
			   .andExpect(status().is4xxClientError());
	}

	@Test
	@WithMockUser(username = "doblee", roles = "ADMIN")
	void test_admin_admin() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/admin"))
			   .andDo(print())
			   .andExpect(status().isOk());
	}

	@Test
	@Transactional
	void test_form_login_success() throws Exception {
		// given
		final String name = "doblee";
		final String password = "123";
		final AccountEntity account = AccountEntity.builder()
												   .name(name)
												   .password(password)
												   .role("USER")
												   .build();

		// when
		accountService.register(account);

		// then
		mockMvc.perform(SecurityMockMvcRequestBuilders.formLogin().user(account.getName()).password(password))
			   .andExpect(authenticated());
	}

	@Test
	@Transactional
	void test_form_login_fail() throws Exception {
		// given
		final String name = "doblee";
		final String password = "123";
		final AccountEntity account = AccountEntity.builder()
												   .name(name)
												   .password(password)
												   .role("USER")
												   .build();

		// when
		accountService.register(account);

		// then
		mockMvc.perform(SecurityMockMvcRequestBuilders.formLogin().user(account.getName()).password("not"))
			   .andExpect(unauthenticated());
	}
}