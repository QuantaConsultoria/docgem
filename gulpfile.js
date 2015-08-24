"use strict";

var gulp   = require("gulp"),
    jade   = require("gulp-jade"),
    watch  = require("gulp-watch"),
    stylus = require("gulp-stylus"),
    es     = require("event-stream");

var paths  = {
    stylesheets: "./src/main/resources/stylesheets",
    templates:   "./src/main/resources",
    webapp:      "./src/main/resources"
};

var targetPaths  = {
    stylesheets: "./target/site/docgem/stylesheets",
    templates:   "./target/site/docgem",
    webapp:      "./target/site/docgem"
};

gulp.task("compile:jade", function() {
  var tmpl = gulp.src(paths.templates + "/**/*.jade")
    .pipe(jade())
    .pipe(gulp.dest(paths.templates))
    .pipe(gulp.dest(targetPaths.templates));

  return es.concat(tmpl);
});

gulp.task("compile:stylus", function() {
  var main = gulp.src(stylus())
    .pipe(gulp.dest(paths.stylesheets))
    .pipe(gulp.dest(targetPaths.stylesheets));

  return es.concat(main);
});

gulp.task("watch:stylus", function() {
    gulp.watch([paths.stylesheets + "/**/*.styl"], function() {
        gulp.start("compile:stylus");
    });
});

gulp.task("watch:jade", function() {
    gulp.watch([paths.templates + "/**/*.jade"], function(files) {
        var source = ( files.path.split("/templates/") )[1],
            dest   = "";

        if(source) {
            dest   = paths.templates + "/" + ( source.split("/") ).length > 1 ? source.split("/")[0] : "";
            source = paths.templates + "/" + source;
        }

        return gulp.src(source)
            .pipe(jade())
            .pipe(gulp.dest(dest));
    });
});

gulp.task("build", ["compile:jade", "compile:stylus"]);
gulp.task("watch", ["build", "watch:stylus", "watch:jade"]);
