/*
 * Copyright (C) Alan Buttars
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
 */
package com.alanbuttars.commons.config.eventbus;

import com.alanbuttars.commons.config.event.Event;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.google.common.eventbus.SubscriberExceptionHandler;

/**
 * Implements the {@link EventBus} interface by utilizing Google Guava's synchronous
 * {@link com.google.common.eventbus.EventBus}.
 * 
 * @author Alan Buttars
 *
 */
public class EventBusSyncImpl implements EventBus {

	private final com.google.common.eventbus.EventBus eventBus;

	/**
	 * See {@link com.google.common.eventbus.EventBus#EventBus()}.
	 */
	public EventBusSyncImpl() {
		this.eventBus = new com.google.common.eventbus.EventBus();
	}

	/**
	 * See {@link com.google.common.eventbus.EventBus#EventBus(String)}.
	 */
	public EventBusSyncImpl(String eventBusId) {
		this.eventBus = new com.google.common.eventbus.EventBus(eventBusId);
	}

	/**
	 * See {@link com.google.common.eventbus.EventBus#EventBus(SubscriberExceptionHandler)}.
	 */
	public EventBusSyncImpl(SubscriberExceptionHandler exceptionHandler) {
		this.eventBus = new com.google.common.eventbus.EventBus(exceptionHandler);
	}

	@VisibleForTesting
	protected com.google.common.eventbus.EventBus getEventBus() {
		return eventBus;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends Event> void publish(E event) {
		getEventBus().post(event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void subscribe(Object object) {
		getEventBus().register(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unsubscribe(Object object) {
		getEventBus().unregister(object);
	}

}
