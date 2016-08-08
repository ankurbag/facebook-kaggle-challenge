<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>FB Checkin Prediction</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href='http://fonts.googleapis.com/css?family=Open+Sans'
	rel='stylesheet' type='text/css'>
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<link
	href="http://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css"
	rel="stylesheet">
<link href='http://fonts.googleapis.com/css?family=Shadows+Into+Light'
	rel='stylesheet' type='text/css'>
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
<style>
rect {
	fill: transparent;
	shape-rendering: crispEdges;
}

.axis path, .axis line {
	fill: none;
	stroke: rgba(0, 0, 0, 0.1);
	shape-rendering: crispEdges;
}

.axisLine {
	fill: none;
	shape-rendering: crispEdges;
	stroke: rgba(0, 0, 0, 0.5);
	stroke-width: 2px;
}

.dot {
	fill-opacity: .5;
}

.d3-tip {
	line-height: 1;
	font-weight: bold;
	padding: 12px;
	background: rgba(0, 0, 0, 0.8);
	color: #fff;
	border-radius: 2px;
}

/* Creates a small triangle extender for the tooltip */
.d3-tip:after {
	box-sizing: border-box;
	display: inline;
	font-size: 10px;
	width: 100%;
	line-height: 1;
	color: rgba(0, 0, 0, 0.8);
	content: "\25BC";
	position: absolute;
	text-align: center;
}

/* Style northward tooltips differently */
.d3-tip.n:after {
	margin: -1px 0 0 0;
	top: 100%;
	left: 0;
}
</style>


</head>
<body>
	<nav class="navbar navbar-inverse navbar-fixed-top"
		style="background-color: #3B5998">
		<div class="container">
			<div class="navbar-header">
				<a class="navbar-brand" href="/fbcheckin/"><span><img
						src="fb_icon.png" />&nbsp;CheckinPredictor</span></a>
			</div>
			<ul class="nav navbar-nav">
				<li class="active"><a href="/fbcheckin/">Home</a></li>

			</ul>
			<form class="navbar-form navbar-right form-inline">
				<div class="input-group">
					<input type="number" min="0" step="0.001" class="form-control"
						name="xcoord" id="xcoord" max="10" placeholder="Enter x coord"
						required
						value=<jsp:expression>request.getParameter("xcoord") </jsp:expression> />
				</div>
				<div class="input-group">
					<input type="number" min="0" max="10" step="0.001"
						class="form-control" name="ycoord" id="ycoord"
						placeholder="Enter y coord" required
						value=<jsp:expression>request.getParameter("ycoord") </jsp:expression> />
					<span class="input-group-btn">
						<button type="submit" class="btn btn-success btn-sm">
							<span class="glyphicon glyphicon-search"></span> Checkin
						</button>
					</span>
				</div>

			</form>
		</div>
	</nav>
	<!-- Navigation -->
	<div>&nbsp;</div>
	<div>&nbsp;</div>
	<div>&nbsp;</div>
	<div>&nbsp;</div>
	<div>&nbsp;</div>
	<div>&nbsp;</div>
	<div>&nbsp;</div>
	<div>&nbsp;</div>
	<div>&nbsp;</div>
	<div class="container">
		<div class="row">
			<div class="col-md-1"></div>
			<div class="panel-group col-md-10">
				<div class="panel panel-info">
					<div class="panel-heading">
						<center>
							<em><strong>10X10 Map</strong></em>
						</center>
					</div>
					<div id="scatter" class="panel-body"></div>

				</div>

			</div>
			<!-- End of Panel -->
			<div class="col-md-1"></div>
		</div>
		<!-- End of row -->
	</div>
	<!-- 
	<center>
	<div id="scatter"></div>
	</center> -->
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
	<script src="http://d3js.org/d3.v3.min.js" charset="utf-8"></script>
	<script
		src="http://labratrevenge.com/d3-tip/javascripts/d3.tip.v0.6.3.js"></script>
	<script>
		$(document)
				.ready(
						function() {

							/** LOAD the Default MAP **/
							var margin = {
								top : 50,
								right : 300,
								bottom : 50,
								left : 50
							}, outerWidth = 1050, outerHeight = 500, width = outerWidth
									- margin.left - margin.right, height = outerHeight
									- margin.top - margin.bottom;

							var x = d3.scale.linear().range([ 0, width ])
									.nice();

							var y = d3.scale.linear().range([ height, 0 ])
									.nice();

							var x_coord = "x_coord", y_coord = "y_coord", loc_id = "loc_id", review = "review", colorCat = "blue";

							var data = 0;
							$.ajax(
									{
										method : "POST",
										url : "getcheckin?xcoord="
												+ $("#xcoord").val()
												+ "&ycoord="
												+ $("#ycoord").val(),
										dataType : "json"
									}).done(function(msg) {
								//alert("SUCCESS");
								console.log(msg);
								data = msg;
								showScatterPlot(data);
							});
							//showScatterPlot(data);

							function showScatterPlot(data) {
								console.log("In Scateer" + data);
								var xMax = d3.max(data, function(d) {
									return d[x_coord];
								}) * 1.05, xMin = d3.min(data, function(d) {
									return d[x_coord];
								}), xMin = xMin > 0 ? 0 : xMin, yMax = d3.max(
										data, function(d) {
											return d[y_coord];
										}) * 1.05, yMin = d3.min(data,
										function(d) {
											return d[y_coord];
										}), yMin = yMin > 0 ? 0 : yMin;

								x.domain([ xMin, xMax ]);
								y.domain([ yMin, yMax ]);

								var xAxis = d3.svg.axis().scale(x).orient(
										"bottom").tickSize(-height);

								var yAxis = d3.svg.axis().scale(y).orient(
										"left").tickSize(-width);

								var color = d3.scale.category10();

								var imagePath = "business.png";
								var image = "<img src= "+ imagePath +" />";

								var tip = d3.tip().attr("class", "d3-tip")
										.offset([ -10, 0 ]).html(
												function(d) {
													//return loc_id + ": " + d[loc_id];
													return loc_id + ": "
															+ d[loc_id]
															+ "<br><br>"
															+ image + " "
															+ d[review];
												});

								var zoomBeh = d3.behavior.zoom().x(x).y(y)
										.scaleExtent([ 0, 500 ]).on("zoom",
												zoom);

								var svg = d3.select("#scatter").append("svg")
										.attr("width", outerWidth).attr(
												"height", outerHeight).append(
												"g").attr(
												"transform",
												"translate(" + margin.left
														+ "," + margin.top
														+ ")").call(zoomBeh);

								svg.call(tip);

								svg.append("rect").attr("width", width).attr(
										"height", height);

								svg.append("g").classed("x axis", true).attr(
										"transform",
										"translate(0," + height + ")").call(
										xAxis).append("text").classed("label",
										true).attr("x", width).attr("y",
										margin.bottom - 10).style(
										"text-anchor", "end").text(x_coord);

								svg.append("g").classed("y axis", true).call(
										yAxis).append("text").classed("label",
										true).attr("transform", "rotate(-90)")
										.attr("y", -margin.left).attr("dy",
												".71em").style("text-anchor",
												"end").text(y_coord);

								var objects = svg.append("svg").classed(
										"objects", true).attr("width", width)
										.attr("height", height);

								objects.append("svg:line").classed(
										"axisLine hAxisLine", true).attr("x1",
										0).attr("y1", 0).attr("x2", width)
										.attr("y2", 0).attr("transform",
												"translate(0," + height + ")");

								objects.append("svg:line").classed(
										"axisLine vAxisLine", true).attr("x1",
										0).attr("y1", 0).attr("x2", 0).attr(
										"y2", height);

								objects.selectAll(".dot").data(data).enter()
										.append("circle").classed("dot", true)
										.attr("r", function(d) {
											return 10;
										}).attr("transform", transform)
										.style("fill", function(d, i) {
											if (i == 0) {
												console.log("index is: " + i);
												return "red";
											} else
												return color(d[colorCat]);
										}).on("mouseover", tip.show).on(
												"mouseout", tip.hide);
								//Code for legend
								var legend = svg.selectAll(".legend").data(
										color.domain()).enter().append("g")
										.classed("legend", true).attr(
												"transform",
												function(d, i) {
													return "translate(0," + i
															* 20 + ")";
												});

								legend.append("circle").attr("r", 3.5).attr(
										"cx", width + 20).attr("fill", "red");

								legend.append("text").attr("x", width + 26)
										.attr("dy", ".35em").text(function(d) {
											console.log("In legend");
											return d;
										});
								//End

								d3.select("input").on("click", change);

								function change() {
									xMax = d3.max(data, function(d) {
										return d[x_coord];
									});
									xMin = d3.min(data, function(d) {
										return d[x_coord];
									});

									zoomBeh.x(x.domain([ xMin, xMax ])).y(
											y.domain([ yMin, yMax ]));

									var svg = d3.select("#scatter")
											.transition();

									svg.select(".x.axis").duration(750).call(
											xAxis).select(".label").text(
											x_coord);

									objects.selectAll(".dot").transition()
											.duration(1000).attr("transform",
													transform);
								}

								function zoom() {
									svg.select(".x.axis").call(xAxis);
									svg.select(".y.axis").call(yAxis);

									svg.selectAll(".dot").attr("transform",
											transform);
								}

								function transform(d) {
									return "translate(" + x(d[x_coord]) + ","
											+ y(d[y_coord]) + ")";
								}
							}
							;

							/** AJAX CALL **/
							$("button").click(
									function() {
										//alert("call AJAX");
										$.ajax(
												{
													method : "POST",
													url : "getcheckin?xcoord="
															+ $("#xcoord")
																	.val()
															+ "&ycoord="
															+ $("#ycoord")
																	.val(),
													dataType : "json"
												}).done(function(msg) {
											//alert("SUCCESS");
											console.log(msg);
											data = msg;
											//showScatterPlot(data);
										});

									});
						});
	</script>


</body>


</html>
