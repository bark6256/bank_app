<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/layout/header.jsp"%>
<div class="col-sm-8">
	<h2>출금 (인증)</h2>
	<div class="bg-light p-md-5 h-75">
		<form action="/account/withdraw" method="post">
			<div class="form-group">
				<label for="amount">출금 금액 (원):</label>
				<input type="number" class="form-control" placeholder="출금 금액"
				        id="amount" name="amount">
			</div><br>
			<div class="form-group">
				<label for="wAccountNumber">계좌 번호:</label>
				<input type="text" class="form-control" placeholder="계좌 번호"
				        id="wAccountNumber" name="wAccountNumber">
			</div><br>
			<div class="form-group">
				<label for="wAccountPassword">계좌 비밀번호:</label>
				<input type="password" class="form-control" placeholder="계좌 비밀번호"
				        id="wAccountPassword" name="wAccountPassword">
			</div><br>
			<button type="submit" class="btn btn-primary">출금</button>
		</form>
	</div>
</div>
<%@ include file="/WEB-INF/view/layout/footer.jsp"%>
