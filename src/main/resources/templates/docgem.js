var drawCharpters = function(charpters) {
	for (var i in charpters) {
		var charpter = charpters[i];
		$.tmpl( $("#charpterTemplate").text(), charpter ).appendTo( "#charpters" );
		$("#index").append("<li>"+charpter.id+"<li>");
		for(var j in charpter.sections) {
			var section = charpter.sections[j];
			$(".charpter[data-id='"+charpter.id+"']" ).find(".sections").append($.tmpl( $("#sectionTemplate").text(), section ));
			for(var k in section.actions) {
				var action = section.actions[k];
				$(".section[data-id='"+section.id+"']" ).find(".actions").append($.tmpl( $("#actionTemplate").text(), action ));
			}
		}
	}
}

$(function(){
	drawCharpters(charpters);
});