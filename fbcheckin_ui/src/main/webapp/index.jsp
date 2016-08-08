<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="en">
<head>
<title>FB Checkin Prediction</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href='http://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet'  type='text/css'>
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
 <script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
	<link href="http://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
<link href='http://fonts.googleapis.com/css?family=Shadows+Into+Light' rel='stylesheet' type='text/css'>
<style>
	.social {
    
    height: 3em;
    width: 13.5em;
    margin: 150px 450px;
}

.social {
    display: block;
    height: 4em;
    line-height: 4em;
    margin: -2.2em;
    
    -webkit-transition: -webkit-transform .7s;
    -moz-transition: -moz-transform .7s;
    -ms-transition: -ms-transform .7s;
    -o-transition: -o-transform .7s;
    transition: transform .7s;
    -webkit-transform: rotate(45deg);
    -moz-transform: rotate(45deg);
    -ms-transform: rotate(45deg);
    -o-transform: rotate(45deg);
    transform: rotate(45deg);
    
    width: 4em;

}

.social a {
    color: #fffdf0;
    display: block;
    height: 4em;
    line-height: 6em;
    
    -webkit-transform: rotate(-45deg);
    -moz-transform: rotate(-45deg);
    -ms-transform: rotate(-45deg);
    -o-transform: rotate(-45deg);
    transform: rotate(-45deg);
    width: 4em; 
  
}
.facebook {
    background: #155b9d;
    left: 0;
    top: 0%;
}

</style>
</head>
<body>
	<nav class="navbar navbar-inverse navbar-fixed-top" style="background-color:#3B5998">
		<div class="container">
			<div class="navbar-header" >
				<a class="navbar-brand" href="/fbcheckin/"><span ><img src="fb_icon.png"/>&nbsp;CheckinPredictor</span></a>
			</div>
			<ul class="nav navbar-nav">
				<li class="active"><a href="/fbcheckin/">Home</a></li>
				
			</ul>
		</div>
	</nav>
	<div class="container">

      	<div class="row">&nbsp;</div>
        <div class="row">&nbsp;</div>
        <div class="row">&nbsp;</div>
        <div class="row">&nbsp;</div>
        <div class="row">&nbsp;</div>
        <div class="row">&nbsp;</div>
        <div class="row">&nbsp;</div>
        <div class="row">&nbsp;</div>

		<div class="form-group" role="form">
			
            <p></p>
            <div class="col-md-3">
            </div>
			<div class="panel-group col-md-6">
				<div class="panel panel-info">
				<center>
                	<span class="facebook social"><a href="http://facebook.com">
                	<i class="fa fa-facebook fa-3x"></i></a></span>
                	<span style="font-family:'Open Sans', Arial; font-weight:bold; font-size:60px">
                    <font color="#4285F4">Ch</font><font color="#EB584C">ec</font><font color="#FCBD05">k</font><font color="#4285F4">i</font><font color="#EB584C">n</font></span>
                    </center>
					
                    
                    <div class="panel-heading"><center><em><strong>The Checkin Predictor</strong></em></center>
					</div>
					<div class="panel-body">

						<!-- Coordinates Search Box -->
						
						<div id="searchForm" class="row">
						<form action="getcheckin" class="form-inline" method="get">
							
							<div class="row col-md-12">
							
								<div class="col-md-1">
							    <label for="pwd">&nbsp;</label>
							     </div>
								<div class="col-md-4">
								<div class="form-group">
							      <label for="email">X:</label>
							      <input type="number" min="0" step="0.001" class="form-control" max="10" name="xcoord" id="xcoord" placeholder="Enter x coord" required />
							    </div>
							    </div>
							    <div class="col-md-4">
							    <div class="form-group">
							      <label for="pwd">Y:</label>
							      <input type="number" min="0" step="0.001" class="form-control" max="10" name="ycoord" id="ycoord" placeholder="Enter y coord" required />
							    </div>
							    </div>
							    <div class="col-md-1">
							    <label for="pwd">&nbsp;</label>
							     </div>
							    <div class="col-md-2">
							    <label for="pwd">&nbsp;</label>
							     
							    <button type="submit" class="btn btn-success btn-sm">
							         <span class="glyphicon glyphicon-search"></span> Checkin
							    </button>
							    </div>
							 
						    </div>
                           						
                        </form>
                        <div></div>
						</div>
						
						<!-- End:Coordinates Search Box -->
					</div>

				</div>

			</div>           
		</div>
	</div>

</body>
</html>
