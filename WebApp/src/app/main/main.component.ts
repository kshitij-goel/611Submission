import { Component, OnInit } from '@angular/core';
import {PubNubAngular} from 'pubnub-angular2';

class TransmitObject {
  message: string;
  deviceType: string;
}

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {

  common = {
    pubKey: 'pub-x-key',
    subKey: 'sub-x-key',
    subscribeChannel: 'Hub Channel',
    publishChannel: 'Mobile Channel',
    pubnub_global: {},

    overrideUnique: '00:00:00:00:00:00',
    transmitObject: new TransmitObject,

    overrideDis: false,
    redDis: false,
    yellowDis: false,
    greenDis: false,
    overrideStatus: false,
    redStatus: false,
    yellowStatus: false,
    greenStatus: false,

    sensorDistance: '',

    pubnubSubscribe: function () {
      const context = this;
      function temp(map: TransmitObject) {
        const test = () => {
          context.ServerTask(map);
        };
        test();
      }

      this.pubnub_global.addListener({
        presence: function (m) {
          console.log(m);
        },
        message: function (message) {
          const extract = message.message;
          console.log('Received in subscribe:');
          console.log(extract);
          temp(extract.map);
        }
      });
      this.pubnub_global.subscribe({
        channels: [this.subscribeChannel],
        withPresence: true
      });
    },

    pubnubPublish: function (transmitObject: TransmitObject) {
      if (!this.overrideDis) {
        this.pubnub_global.publish(
          {
            message: transmitObject,
            channel: this.publishChannel
          },
          function (status, response) {
            if (status.error) {
              console.log(status);
            } else {
              console.log('message Published w/ timetoken', response.timetoken);
            }
          }
        );
      }
    },

    ServerTask: function (trans: TransmitObject) {
      console.log('here');
      const recs = trans.message.split('#');
      if (trans.deviceType.localeCompare('android') === 0 || trans.deviceType.localeCompare('webapp') === 0) {
        if (recs[3].localeCompare('1') === 0 && recs[1].localeCompare(this.overrideUnique) !== 0) {
          this.setDis('dis', 'dis', 'dis', 'dis');
        } else if (recs[3].localeCompare('0') === 0 && recs[1].localeCompare(this.overrideUnique) !== 0) {
          this.setDis('en', 'dis', 'dis', 'dis');
        }
      } else if (trans.deviceType.localeCompare('hub') === 0) {
        if (!this.overrideDis) {
          this.setStatus(recs[3], recs[5], recs[7]);
          this.sensorDistance = recs[9];
        } else {
          if (this.overrideStatus) {
            console.log('no update');
          } else {
            this.setStatus(recs[3], recs[5], recs[7]);
            this.sensorDistance = recs[9];
          }
        }
      }
      console.log('red: ' + this.redStatus);
      console.log('yellow: ' + this.yellowStatus);
      console.log('green: ' + this.greenStatus);
    },

    ClientTask: function (transmitObject: TransmitObject) {
      this.pubnubPublish(transmitObject);
    },

    overrideClick: function () {
      if (!this.overrideDis) {
        this.overrideStatus = !this.overrideStatus;
        this.redDis = !this.redDis;
        this.yellowDis = !this.yellowDis;
        this.greenDis = !this.greenDis;
        if (this.overrideStatus === true) {
          const msg = 'webapp#' + this.overrideUnique + '#override#' + this.getStatus('override') + '#red#' + this.getStatus('red');
          const msg2 = msg + '#yellow#' + this.getStatus('yellow') + '#green#' + this.getStatus('green');
          this.transmitObject.message = msg2;
          this.ClientTask(this.transmitObject);
        } else {
          if (this.redStatus) {
            document.getElementById('redSlider').click();
          }
          if (this.yellowStatus) {
            document.getElementById('yellowSlider').click();
          }
          if (this.greenStatus) {
            document.getElementById('greenSlider').click();
          }
          this.redStatus = false;
          this.yellowStatus = false;
          this.greenStatus = false;
          const msg = 'webapp#' + this.overrideUnique + '#override#' + this.getStatus('override') + '#red#' + this.getStatus('red');
          const msg2 = msg + '#yellow#' + this.getStatus('yellow') + '#green#' + this.getStatus('green');
          this.transmitObject.message = msg2;
          this.ClientTask(this.transmitObject);
        }
      }
    },

    redClick: function () {
      this.redStatus = !this.redStatus;
      console.log(this.redStatus);
      console.log(this.redDis);
      if (this.redStatus === true && this.redDis === false) {
        const msg = 'webapp#' + this.overrideUnique + '#override#' + this.getStatus('override') + '#red#' + this.getStatus('red');
        const msg2 = msg + '#yellow#' + this.getStatus('yellow') + '#green#' + this.getStatus('green');
        this.transmitObject.message = msg2;
        // this.transmitObject.setMessage(msg2);
        this.ClientTask(this.transmitObject);
      } else if (this.redDis === false) {
        const msg = 'webapp#' + this.overrideUnique + '#override#' + this.getStatus('override') + '#red#' + this.getStatus('red');
        const msg2 = msg + '#yellow#' + this.getStatus('yellow') + '#green#' + this.getStatus('green');
        this.transmitObject.message = msg2;
        // this.transmitObject.setMessage(msg2);
        this.ClientTask(this.transmitObject);
      }
    },

    yellowClick: function () {
      this.yellowStatus = !this.yellowStatus;
      console.log(this.yellowStatus);
      if (this.yellowStatus === true && this.yellowDis === false) {
        const msg = 'webapp#' + this.overrideUnique + '#override#' + this.getStatus('override') + '#red#' + this.getStatus('red');
        const msg2 = msg + '#yellow#' + this.getStatus('yellow') + '#green#' + this.getStatus('green');
        this.transmitObject.message = msg2;
        // this.transmitObject.setMessage(msg2);
        this.ClientTask(this.transmitObject);
      } else if (this.yellowDis === false) {
        const msg = 'webapp#' + this.overrideUnique + '#override#' + this.getStatus('override') + '#red#' + this.getStatus('red');
        const msg2 = msg + '#yellow#' + this.getStatus('yellow') + '#green#' + this.getStatus('green');
        this.transmitObject.message = msg2;
        // this.transmitObject.setMessage(msg2);
        this.ClientTask(this.transmitObject);
      }
    },

    greenClick: function () {
      this.greenStatus = !this.greenStatus;
      console.log(this.greenStatus);
      if (this.greenStatus === true && this.greenDis === false) {
        const msg = 'webapp#' + this.overrideUnique + '#override#' + this.getStatus('override') + '#red#' + this.getStatus('red');
        const msg2 = msg + '#yellow#' + this.getStatus('yellow') + '#green#' + this.getStatus('green');
        this.transmitObject.message = msg2;
        // this.transmitObject.setMessage(msg2);
        this.ClientTask(this.transmitObject);
      } else if (this.greenDis === false) {
        const msg = 'webapp#' + this.overrideUnique + '#override#' + this.getStatus('override') + '#red#' + this.getStatus('red');
        const msg2 = msg + '#yellow#' + this.getStatus('yellow') + '#green#' + this.getStatus('green');
        this.transmitObject.message = msg2;
        // this.transmitObject.setMessage(msg2);
        this.ClientTask(this.transmitObject);
      }
    },

    getStatus: function (str: string) {
      if (str.localeCompare('override') === 0) {
        if (this.overrideStatus) {
          return '1';
        } else {
          return '0';
        }
      } else if (str.localeCompare('red') === 0) {
        if (this.redStatus) {
          return '1';
        } else {
          return '0';
        }
      } else if (str.localeCompare('yellow') === 0) {
        if (this.yellowStatus) {
          return '1';
        } else {
          return '0';
        }
      } else if (str.localeCompare('green') === 0) {
        if (this.greenStatus) {
          return '1';
        } else {
          return '0';
        }
      }
    },

    setStatus: function (red: string, yellow: string, green: string) {
      if (red.localeCompare('0') === 0) {
        this.redStatus = false;
      } else if (red.localeCompare('1') === 0) {
        this.redStatus = true;
      }
      if (yellow.localeCompare('0') === 0) {
        this.yellowStatus = false;
      } else if (yellow.localeCompare('1') === 0) {
        this.yellowStatus = true;
      }
      if (green.localeCompare('0') === 0) {
        this.greenStatus = false;
      } else if (green.localeCompare('1') === 0) {
        this.greenStatus = true;
      }
    },

    setDis: function (over: string, red: string, yellow: string, green: string) {
      if (over.localeCompare('dis') === 0) {
        this.overrideDis = true;
      } else if (over.localeCompare('en') === 0) {
        this.overrideDis = false;
      }
      if (red.localeCompare('dis') === 0) {
        this.redDis = true;
      } else if (red.localeCompare('en') === 0) {
        this.redDis = false;
      }
      if (yellow.localeCompare('dis') === 0) {
        this.yellowDis = true;
      } else if (yellow.localeCompare('en') === 0) {
        this.yellowDis = false;
      }
      if (green.localeCompare('dis') === 0) {
        this.greenDis = true;
      } else if (green.localeCompare('en') === 0) {
        this.greenDis = false;
      }
    }
  };
  constructor(pubnub: PubNubAngular) {
    pubnub.init({publishKey: this.common.pubKey, subscribeKey: this.common.subKey});
    pubnub.grant({
        channels: [this.common.publishChannel, this.common.subscribeChannel],
        authKeys: [this.common.pubKey, this.common.subKey],
        ttl: 5,
        read: true,
        write: true
      },
      (status) => {
        console.log(status);
      });
    this.common.pubnub_global = pubnub;
  }

  ngOnInit() {
    this.common.redDis = true;
    this.common.yellowDis = true;
    this.common.greenDis = true;
    this.common.transmitObject.deviceType = 'webapp';
    this.common.pubnubSubscribe();
  }
}
