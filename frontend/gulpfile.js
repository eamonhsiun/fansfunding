var gulp = require('gulp'),
  sass = require('gulp-sass'),
  autoprefixer = require('gulp-autoprefixer'),
  notify = require('gulp-notify'),
  livereload = require('gulp-livereload'),
  webserver = require('gulp-webserver'),
  fileinclude = require('gulp-file-include');

gulp.task('default', function() {
  gulp.run('watch');
});

gulp.task('sass', function() {
    gulp.src('dev/scss/*.scss')
        .pipe(sass({ style: 'compressed'/*,outputStyle: 'compressed'*/}))
        .pipe(autoprefixer('last 2 version', 'safari 5', 'ie 8', 'ie 9', 'opera 12.1', 'ios 6', 'android 4'))
        .pipe(gulp.dest('dist/static/css'))
        .pipe(notify({ message: 'Styles task complete' }))
        .pipe(livereload());
});

gulp.task('fileinclude', function() {
  gulp.src('dev/html/*.html')
      .pipe(fileinclude({prefix: '@@',basepath: '@file'}))
      .pipe(gulp.dest('dist/'))
      .pipe(notify({message: 'fileinclude task complete'}))
      .pipe(livereload());
});

gulp.task('js', function() {
  gulp.src('dev/js/*.js')
      .pipe(gulp.dest('dist/static/js/'));
  gulp.src('dev/js/module/*.js')
      .pipe(gulp.dest('dist/static/js/module/'))
      // .pipe(notify({message: 'js remove complete'}))
      .pipe(livereload());
});

gulp.task('webserver', function(){
  gulp.src('dist/')
    .pipe(webserver({
        livereload: true,
        open: true
    }));
});

gulp.task('watch',function(){
  livereload.listen();
  gulp.watch(['dev/scss/*.scss'],['sass']);
  gulp.watch(['dev/html/*.html'],['fileinclude']);
  gulp.watch(['dev/js/*.js','dev/js/module/*.js'],['js']);
  // gulp.watch(['*.html']).on('change', livereload.changed);
});
