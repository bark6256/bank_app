<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/layout/header.jsp"%>
<div class="col-sm-8">
	<h2>계좌 목록 (인증)</h2>
	<div class="bg-light p-md-5 h-75">
		<c:choose>
			<c:when test="${empty accountList}">
				<p>생성된 계좌가 없습니다.</p>
			</c:when>
			<c:otherwise>
				<table class="table">
					<thead>
						<tr>
							<th>계좌번호</th>
							<th>금액</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="account" items="${accountList}">
							<tr>
								<td>${account.number}</td>
								<td>${account.balance}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:otherwise>
		</c:choose>
	</div>
</div>
<%@ include file="/WEB-INF/view/layout/footer.jsp"%>
