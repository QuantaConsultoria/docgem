var Action = {
		NEXT: 'NEXT',
		BACK: 'BACK'
};

var getChapter = function(chapterID) {
	for(var i in chapters) {
		var chapter = chapters[i];
		if(chapter.id === chapterID) {
			return chapter;
		}
	}
	throw "Capítulo não encontrado.";
};

var getSection = function(chapterID, sectionID) {
	var chapter = getChapter(chapterID);
	for(var i in chapter.sections) {
		var section = chapter.sections[i];
		if(section.id === sectionID) {
			return section;
		}
	}
	throw "Seção não encontrada.";
};

var drawChapters = function(chapters) {
	$("#index").html(tmpl("menuTemplate", {chapters: chapters}));
	for (var i in chapters) {
		var chapter = chapters[i];

		for(var j in chapter.sections) {
			var section = chapter.sections[j];
			for(var k in section.actions) {
				var action = section.actions[k];
				action.index = k;
			}
		}
		$("#chapters").html(tmpl("chapterTemplate", {chapter: chapter}));
	}
};

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

var selectSection = function(e) {
	e.preventDefault();
	var sectionId = $(this).data("id");
	var chapterId = $(this).data("chapter");
	var section = getSection(chapterId, sectionId);

	var divSections = $(".sections[data-chapter='" + chapterId + "']");
	divSections.html(tmpl("sectionTemplate", {section: section}));

	divSections.find('.action[data-index="0"]').removeClass("disabled");
	divSections.find('.action[data-index="0"]').addClass("active");

	loadFunction();
	validateAction(-1, Action.NEXT, sectionId);
};

var selectChapter = function(e) {
	var chapterId = $(this).data("id");

	var currentChapter = $(".chapter.active");
	var selectedChapter = $(".chapter[data-id='" + chapterId + "']");

	disableElement(currentChapter);
	enableElement(selectedChapter);
};

var enableElement = function(element) {
	$(element).removeClass("disabled");
	$(element).addClass("active");
};

var disableElement = function(element) {
	$(element).removeClass("active");
	$(element).addClass("disabled");
};

var validateAction = function(index, action, sectionId) {
	var qtdActions = $("div[data-id='" + sectionId + "']").find(".action").length;
	var buttonback = $("div[data-id='" + sectionId + "']").find(".back-action");
	var buttonNext = $("div[data-id='" + sectionId + "']").find(".next-action");

	if(qtdActions == 1) {
		$(buttonback).attr("disabled", "disabled");
		$(buttonNext).attr("disabled", "disabled");
	} else 	if(action == Action.NEXT) {
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

		if(index === 0) {
			$("div[data-id='" + sectionId + "']").find(".back-action").attr("disabled", "disabled");
		}
	}
};

var loadFunction = function() {
	$(".back-action").click(previousAction);
	$(".next-action").click(nextAction);
};

$(function(){
	drawChapters(chapters);
	$(".section-menu").click(selectSection);
	$(".chapter-menu").click(selectChapter);
});