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

/**
 * Models a simple event bus which supports publish-subscribe communication.
 * 
 * @author Alan Buttars
 *
 */
public interface EventBus {

	/**
	 * Publishes an event to the event bus.
	 * 
	 * @param <E>
	 * 
	 * @param event
	 *            Non-null event
	 */
	public <E extends Event> void publish(E event);

	/**
	 * Subscribes to events published by this event bus.
	 * 
	 * @param object
	 *            Non-null object
	 */
	public void subscribe(Object object);

	/**
	 * Unsubscribes to events published by this event bus. If the object is already unsubscribed, this method does
	 * nothing.
	 * 
	 * @param event
	 *            Non-null object
	 */
	public void unsubscribe(Object object);
}
