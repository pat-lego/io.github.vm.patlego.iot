const events = Vue.createApp({
    el: '#app',
    data() {
        return {
            sensordata: {}
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
    }
});

events.mount('#app');