<!DOCTYPE html>
<html>
<#include "header.ftl">
<div>this is index112</div>
<#list vos as item>
   Part repeated for each item
<#else>
Part executed when there are 0 items
</#list>
</html>

<html>
<head>
    <title>Welcome!</title>
</head>
<body>
<h1>Welcome John Doe!</h1>
<p>Our latest product:
    <a href="products/greenmouse.html">green mouse</a>!
</body>
</html>

<html>
<head>
    <title>Welcome!</title>
</head>
<body>
<h1>Welcome John Doe!</h1>
<p>Our latest product:
    <a href="products/greenmouse.html">green mouse</a>!
</body>
</html>