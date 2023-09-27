<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/layout/header.jsp"%>
<!-- end of header.jsp -->
<!-- start of body -->
<div class="col-sm-8">
	<h2>로그인</h2>
	<h5>어서오세요 환영합니다</h5>
	<div class="bg-light p-md-5 h-75">
		<form action="/user/sign-in" method="post">
			<div class="form-group">
				<label for="username">Username:</label>
				<input type="text" class="form-control" placeholder="Enter username"
				        id="username" name="username" value="aaaa">
			</div>
			<div class="form-group">
				<label for="pwd">Password:</label>
				<input type="password" class="form-control" placeholder="Enter password"
				        id="pwd" name="password" value="1234">
			</div>
			<button type="submit" class="btn btn-primary">Login</button>
			<a href="https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=891be07cd472112a139584ce6aec7832&redirect_uri=http://localhost:80/user/kakao/callback">
			<img alt="카카오 로그인" src="/images/kakao_login_small.png" width="74" height="38"> </a>
		</form>
	</div>
</div>
<!-- end of body -->
<!-- start of footer.jsp -->
<%@ include file="/WEB-INF/view/layout/footer.jsp"%>
