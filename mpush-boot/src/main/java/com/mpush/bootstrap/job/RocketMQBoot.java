/*
 * (C) Copyright 2015-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *   ohun@live.cn (夜色)
 */

package com.mpush.bootstrap.job;

import com.mpush.core.mqpubsub.RocketMQConsumer;
import com.mpush.core.mqpubsub.RocketMQProductFactory;
import com.mpush.tools.log.Logs;

/**
 * Created by nydia on 2022/10/22.
 *
 * @author nydia@hotmail.com
 */
public final class RocketMQBoot extends BootJob {
    public RocketMQBoot() {

    }

    @Override
    public void start() {
        try {
            RocketMQConsumer rocketMQConsumer = new RocketMQConsumer();
            rocketMQConsumer.start();

            RocketMQProductFactory.init();
        } catch (Exception e) {
            Logs.Console.error("加载rocketmq失败:" + e.getMessage(), e);
        }
        startNext();
    }

    @Override
    protected void stop() {
    }
}
