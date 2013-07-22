var Action = {
		NEXT: 'NEXT',
		BACK: 'BACK'
};

var drawChapters = function(chapters) {
	for (var i in chapters) {
		var chapter = chapters[i];
		$.tmpl( $("#chapterTemplate"), chapter ).appendTo( "#chapters" );
		$("#index").append("<li>"+chapter.id+"</li>");
		for(var j in chapter.sections) {
			var section = chapter.sections[j];
			$(".chapter[data-id='"+chapter.id+"']" ).find(".sections").append($.tmpl( $("#sectionTemplate"), section ));
			for(var k in section.actions) {
				var action = section.actions[k];
				action.index = k;
				$(".section[data-id='"+section.id+"']" ).find(".actions").append($.tmpl( $("#actionTemplate"), action));
			}
		}
		$(".action[data-index='0']").removeClass("disabled");
		$(".action[data-index='0']").addClass("active");
	}
}

var previousAction = function(e) {
	var sectionId = $(this).data("id");
	e.preventDefault();
	var lastAction = $("div[data-id='" + sectionId + "']").find(".action.active");
	var lastIndex = $(lastAction).data("index");
	validateAction(lastIndex, Action.BACK, sectionId);
	
	$(lastAction).removeClass("active");
	$(lastAction).addClass("disabled");
	$("div[data-id='" + sectionId + "']").find(".action[data-index='" + (lastIndex - 1) + "']").addClass("active");
	$("div[data-id='" + sectionId + "']").find(".action[data-index='" + (lastIndex - 1) + "']").removeClass("disabled");
};


var nextAction = function(e) {
	var sectionId = $(this).data("id");
	e.preventDefault();
	var lastAction = $("div[data-id='" + sectionId + "'] .active");
	var lastIndex = $(lastAction).data("index");
	validateAction(lastIndex, Action.NEXT, sectionId);
	
	$(lastAction).removeClass("active");
	$(lastAction).addClass("disabled");
	$("div[data-id='" + sectionId + "']").find(".action[data-index='" + (lastIndex + 1) + "']").addClass("active");
	$("div[data-id='" + sectionId + "']").find(".action[data-index='" + (lastIndex + 1) + "']").removeClass("disabled");
};

var validateAction = function(index, action, sectionId) {
	var qtdActions = $("div[data-id='" + sectionId + "']").find(".action").length;
	
	if(action == Action.NEXT) {
		index = index + 1;
		if(index > 0) {
			$("div[data-id='" + sectionId + "']").find(".back-action").removeAttr("disabled");
		}
		
		if(index == qtdActions -1) {
			$("div[data-id='" + sectionId + "']").find(".next-action").attr("disabled", "disabled");
		}
	} else {
		index = index - 1;
		
		if(index < qtdActions -1) {
			$("div[data-id='" + sectionId + "']").find(".next-action").removeAttr("disabled");
		}
		
		if(index == 0) {
			$("div[data-id='" + sectionId + "']").find(".back-action").attr("disabled", "disabled");
		}
	}
};

$(function(){
	drawChapters(chapters);
	
	$(".back-action").click(previousAction);
	$(".next-action").click(nextAction)
});