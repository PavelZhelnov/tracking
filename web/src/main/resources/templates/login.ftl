<#assign page_title = "Login">
<#assign hide_top_nav = true >
<#include "header.ftl"/>
<fieldset>
    <legend>Login:</legend>
    <form id="showOrderInfo" action="${contextPath}/tmw/login" method="post">
        <div class="input-prepend input-append" style="position: relative;">
            <span class="add-on" style="height: 20px;width: 70px;">Username:</span>
            <input id="username" type="text" style="height: 30px;" name="username" value="${username!""}"/>
        </div><br/>
        <div class="input-prepend input-append" style="position: relative;">
            <span class="add-on" style="height: 20px;width: 70px;">Password:</span>
            <input type="password" style="height: 30px;" name="password"/>
        </div><br/>
        <button type="submit" class="btn btn-info">Login</button>
        <#if error??><span id="loginErrorMessage" style="color: #d14;">Username or password is invalid</span></#if>
    </form>
</fieldset>
<script language="javascript">
    document.getElementById("username").focus();
</script>

<#include "footer.ftl"/>
