<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/layout/header.jsp"%>
<div class="col-sm-8">
	<h2>계좌 생성 (인증)</h2>
	<div class="bg-light p-md-5 h-75">
		<form action="/account/save" method="post">
			<div class="form-group">
				<label for="number">계좌 번호:</label>
				<input type="text" class="form-control" placeholder="계좌 번호"
				        id="number" name="number">
			</div><br>
			<div class="form-group">
				<label for="password">계좌 비밀번호:</label>
				<input type="password" class="form-control" placeholder="계좌 비밀번호"
				        id="password" name="password">
			</div><br>
			<div class="form-group">
				<label for="balance">입금 금액 (원):</label>
				<input type="number" class="form-control" placeholder="입금 금액"
				        id="balance" name="balance">
			</div><br>
			<button type="submit" class="btn btn-primary">생성</button>
		</form>
	</div>
</div>
<%@ include file="/WEB-INF/view/layout/footer.jsp"%>
