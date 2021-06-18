const events = Vue.createApp({
    el: '#app',
    data() {
        return {
            sensordata: [],
            metadata: {
                sort: {
                    sensorId: 'asc',
                    time: 'asc',
                    location: 'asc',
                    type: 'asc',
                    thread: 'asc'
                }
            },
            search: ''
        }
    },
    async mounted() {
        await this.getSensorData();
    },
    methods: {
        async getSensorData() {
            try {
                this.sensordata = (await axios.get('/cxf/sensors/events', {
                    headers: {
                        'Authorization': this.getQueryParam('token')
                    }
                })).data;
            } catch (e) {
                window.location.href = '/iot/patlego/login.html';
            }
        },
        getDate(epoch) {
            return moment(epoch).format('dddd, MMMM Do, YYYY h:mm:ss A');
        },
        getQueryParam(name, url = window.location.href) {
            name = name.replace(/[\[\]]/g, '\\$&');
            var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
                results = regex.exec(url);
            if (!results) return null;
            if (!results[2]) return '';
            return decodeURIComponent(results[2].replace(/\+/g, ' '));
        },
        sort(column) {
            if (this.metadata.sort[column] === 'asc') {
                this.sensordata = this.sensordata.sort((a,b) => this.descCompare(a,b,column));
                this.metadata.sort[column] = 'desc';
            } else {
                this.sensordata = this.sensordata.sort((a,b) => this.ascCompare(a,b,column));
                this.metadata.sort[column] = 'asc';
            }

        },
        descCompare (a, b, column) {
            if (a[column] < b[column]) {
                return 1;
            }
            if (a[column] > b[column]) {
                return -1;
            }
            return 0;
        },
        ascCompare (a, b, column) {
            if (a[column] > b[column]) {
                return 1;
            }
            if (a[column] < b[column]) {
                return -1;
            }
            return 0;
        }
    },
    computed: {
        filteredRows() {
            if (this.search && this.search !== '') {
                return this.sensordata.filter(entry => {
                    // Deep clone the object 
                    var copy = JSON.parse(JSON.stringify(entry));
                    
                    copy.time = this.getDate(entry.time);
                    return JSON.stringify(copy).includes(this.search);
                });
            } else {
                return this.sensordata;
            }
        }
    }
});

events.mount('#app');