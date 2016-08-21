Vue.component("pagination", {
    template: '<div>' +
        '<ul class="kit-pagination" v-if="pagination.pages > 1">' +
            '<li :class="{\'disabled\': pagination.pageNum == 1}">' +
                '<a href="#" aria-label="Previous" @click.prevent="changePage(pagination.pageNum - 1)">' +
                    '<span aria-hidden="true" class="prev-btn"></span>' +
                '</a>' +
            '</li>' +
            '<li v-for="num in array" :class="{\'active\': num == pagination.pageNum}">' +
                '<a href="#" @click.prevent="changePage(num)">{{ num }}</a>' +
            '</li>' +
            '<li :class="{\'disabled\': pagination.pageNum >= pagination.pages }">' +
                '<a href="#" aria-label="Next" @click.prevent="changePage(pagination.pageNum + 1)">' +
                    '<span aria-hidden="true" class="next-btn"></span>' +
                '</a>' +
            '</li>' +
        '</ul>' +
    '</div>',
    props: {
        pagination: {
            type: Object,
            required: true
        },
        callback: {
            type: Function,
            required: true
        },
        offset: {
            type: Number,
            default: 3
        }
    },
    computed: {
        array: function () {
            if(this.pagination.pages == 1) {
                return [];
            }
            var from = this.pagination.pageNum - this.offset;
            if(from < 1) {
                from = 1;
            }
            var to = from + (this.offset * 2);
            if(to >= this.pagination.pages) {
                to = this.pagination.pages;
            }
            var arr = [];
            while (to >= from) {
                arr.unshift(to);
                to--;
            }
            while(arr.length < (this.offset * 2 + 1)){
                if(to >= 1){
                    arr.unshift(to);
                    to--
                }else{
                    break;
                }
            }
            return arr;
        }
    },
    watch: {

    },
    methods: {
        changePage: function (page) {
            if(page < 0 || page > this.pagination.pages){
                return;
            }
            this.$set('pagination.pageNum', page);
            this.callback(page);
        }
    }
});
