'use strict';
var gulp = require('gulp'),
    sass = require('gulp-sass'),
    cssnano = require('gulp-cssnano'),
    htmlmin = require('gulp-htmlmin'),
    lint = require('gulp-jshint'),
    concat = require('gulp-concat'),
    uglify = require('gulp-uglify'),
    jshint = require('gulp-jshint'),
    rename = require('gulp-rename'),
    imagemin = require('gulp-imagemin');



gulp.task('sass', function () {
    gulp.src('src/css/*.scss')
        .pipe(rename({
            suffix: ".min",
            extname: ".css"
        }))
        .pipe(sass({outputStyle: 'compressed'}))
        .pipe(gulp.dest('source/css'));
});

gulp.task('scripts', function () {
    gulp.src('src/js/*.js')
        .pipe(rename({
            suffix: ".min",
            extname: ".js"
        }))
        .pipe(jshint())
        .pipe(uglify())
        .pipe(gulp.dest('source/js'))
        .on('error', function(err) {
            console.error('Error in compress task', err.toString());
        });
});


gulp.task('default', function () {
    gulp.run( 'sass', 'scripts','html','images');
    gulp.watch('src/*/*', function () {
        gulp.run( 'sass', 'scripts','images');
    });
    gulp.watch('src/*.html', ['html']);
});

gulp.task('html', function () {
     gulp.src('src/*.html')
        .pipe(htmlmin({collapseWhitespace: true}))
        .pipe(gulp.dest('pages'))
});

gulp.task('images', function () {
        gulp.src('src/img/*')
            .pipe(imagemin())
            .pipe(rename({
                suffix: ".min",
                extname: ".png"
            }))
            .pipe(gulp.dest('source/img'))
}
);