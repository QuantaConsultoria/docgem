var drawChapters = function(chapters) {
	for (var i in chapters) {
		var chapter = chapters[i];
		$.tmpl( $("#chapterTemplate").text(), chapter ).appendTo( "#chapters" );
		$("#index").append("<li>"+chapter.id+"<li>");
		for(var j in chapter.sections) {
			var section = chapter.sections[j];
			$(".chapter[data-id='"+chapter.id+"']" ).find(".sections").append($.tmpl( $("#sectionTemplate").text(), section ));
			for(var k in section.actions) {
				var action = section.actions[k];
				$(".section[data-id='"+section.id+"']" ).find(".actions").append($.tmpl( $("#actionTemplate").text(), action ));
			}
		}
	}
}

$(function(){
	drawChapters(chapters);
});