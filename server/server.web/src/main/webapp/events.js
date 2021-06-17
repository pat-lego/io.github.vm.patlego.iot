const events = Vue.createApp({
    el: '#app',
    data() {
        return {
            sensordata: {},
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
        }
    },
    computed: {
        filteredRows() {
            if (this.search && this.search !== '') {
                const results = Object.entries(this.sensordata).filter(entry => {
                    // Deep clone the object 
                    var copy = JSON.parse(JSON.stringify(entry));
                    
                    copy[1].time = this.getDate(entry[1].time);
                    return JSON.stringify(copy[1]).includes(this.search);
                });
                return Object.fromEntries(results);
            } else {
                return this.sensordata;
            }
        }
    }
});

events.mount('#app');