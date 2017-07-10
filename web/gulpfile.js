//*********** IMPORTS *****************
var gulp = require('gulp'),
	clean = require('gulp-clean'),
    useref = require('gulp-useref'),
    gulpif = require('gulp-if'),
    uglify = require('gulp-uglify'),
    minifyJs = require('gulp-minify'),
    minifyCss = require('gulp-clean-css');

gulp.task('remove-existing', function() {
	return gulp.src('dist')
			.pipe(clean());
})		

gulp.task('copy', ['remove-existing'], function () {
    return gulp.src('studio/**/*')
        .pipe(gulp.dest('dist'));
});

gulp.task('html', ['copy'], function () {
    return gulp.src('dist/app.html')
        .pipe(useref())
        .pipe(gulpif('*.js', uglify()))
        //.pipe(gulpif('*.js', minifyJs()))
        .pipe(gulpif('*.css', minifyCss()))
        .pipe(gulp.dest('dist'));
});

gulp.task('default', ['html']);
