<#import "/spring.ftl" as spring />
<#assign xhtmlCompliant = true in spring>
<!DOCTYPE html>
<html>
<head>
    <title>User Page</title>
</head>
<body>

<header>
     <a href="/logout" id="logout">Logout</a>
</header>

<h1>User Page</h1>
<p>사용자</p>
<#list users as name, value>
  <p>${name}: ${value?string}
</#list><br><br>

<p>사용자 정보</p>
<#list userInfo?keys as key> 
  <p>${key}: ${userInfo[key]?if_exists} 
</#list>
</body>
</html>
