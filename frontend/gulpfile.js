var gulp = require('gulp'),
  sass = require('gulp-sass'),
  autoprefixer = require('gulp-autoprefixer'),
  notify = require('gulp-notify'),
  livereload = require('gulp-livereload'),
  webserver = require('gulp-webserver');

gulp.task('default', function() {
  // 将你的默认的任务代码放在这
  // gulp.run('sass');
  gulp.run('watch');
});

gulp.task('sass', function() {
    gulp.src('static/scss/*.scss')
        .pipe(sass({ style: 'compressed'/*,outputStyle: 'compressed'*/}))
        .pipe(autoprefixer('last 2 version', 'safari 5', 'ie 8', 'ie 9', 'opera 12.1', 'ios 6', 'android 4'))
        .pipe(gulp.dest('static/css'))
        .pipe(notify({ message: 'Styles task complete' }))
        .pipe(livereload());
});

gulp.task('webserver', function(){
  gulp.src('./')
    .pipe(webserver({
        livereload: true,
        open: true
    }));
});

gulp.task('watch',function(){
  livereload.listen();
  gulp.watch(['static/scss/*.scss'],['sass']);
  gulp.watch(['*.html']).on('change', livereload.changed);
});
