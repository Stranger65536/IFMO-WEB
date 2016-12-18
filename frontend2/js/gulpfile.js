/**
 * Created by developer123 on 18.03.15.
 */
var gulp = require('gulp');
var browserify = require('browserify');
var _ = require('underscore');
var source = require('vinyl-source-stream');

gulp.task("browserify", function () {
    var destDir = "./dist";

    var bundleThis = function (srcArray) {
        _.each(srcArray, function (sourceFile) {
            var bundle = browserify(["./src/" + sourceFile + ".js"])
                .bundle()
                .pipe(source(sourceFile + "Bundle.js"))
                .pipe(gulp.dest(destDir));
        });
    };

    bundleThis(["login","notify","calendar"]);
});