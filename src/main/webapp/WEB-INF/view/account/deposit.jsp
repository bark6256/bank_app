<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/layout/header.jsp"%>
<div class="col-sm-8">
	<h2>입금 (인증)</h2>
	<div class="bg-light p-md-5 h-75">
		<!-- 		<form action="" method="post">
			<div class="form-group">
				<label for="amount">입금 금액 (원):</label>
				<input type="number" class="form-control" placeholder="입금 금액"
				        id="amount" name="amount">
			</div><br>
			<div class="form-group">
				<label for="dAccountNumber">계좌 번호:</label>
				<input type="text" class="form-control" placeholder="계좌 번호"
				        id="dAccountNumber" name="dAccountNumber">
			</div>
			<button type="submit" class="btn btn-primary">입금</button>
		</form> -->
		<c:choose>
			<c:when test="${empty accountList}">
				<p>생성된 계좌가 없습니다.</p>
			</c:when>
			<c:otherwise>
				<form action="" method="post">
					<div class="form-group">
						<label for="dAccountNumber">계좌 번호:</label>
						<select id="dAccountNumber" name="dAccountNumber">
							<c:forEach var="account" items="${accountList}">
								<option class="form-control" value="${account.number}">${account.number} </option>
							</c:forEach>
						</select>
					</div>
					<br>
					<div class="form-group">
						<label for="amount">입금 금액 (원):</label> <input type="number"
							class="form-control" placeholder="입금 금액" id="amount"
							name="amount">
					</div>
					<button type="submit" class="btn btn-primary">입금</button>
				</form>
			</c:otherwise>
		</c:choose>
	</div>
</div>
<%@ include file="/WEB-INF/view/layout/footer.jsp"%>
