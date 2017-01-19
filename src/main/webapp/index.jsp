<html>
<head>
<title>Hello World</title>
</head>
<body>
	<h1>Hello World From Struts2</h1>
	<form action="hello">
		<label for="name">Please enter your name</label><br />
		<input type="text" name="name" />
		<input type="submit" value="Say Hello" />
	</form>
	<hr/>

	<h1>hello this is for file upload</h1>
	<form action="upload" method="POST" enctype="multipart/form-data">
		<label for="file">upload your file:</label>
		<input type="file" name="file" />
		<input type="submit" value="upload"/>
	</form>
	
	
	<s:form action="upload" namespace="/" method="POST" enctype="multipart/form-data">
		<s:file name="file" label="Select a File to upload" size="40" />
		<s:submit value="submit" name="submit" />
	</s:form>
</body>
</html>