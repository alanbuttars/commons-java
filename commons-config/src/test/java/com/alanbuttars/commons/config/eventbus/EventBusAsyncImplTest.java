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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.concurrent.Executors;

import org.junit.Before;
import org.junit.Test;

import com.alanbuttars.commons.config.event.Event;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;

/**
 * Test class for {@link EventBusSyncImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class EventBusAsyncImplTest {

	private AsyncEventBus mockEventBus;

	@Before
	public void setup() {
		mockEventBus = mock(AsyncEventBus.class);
	}

	@Test
	public void testConstructorExecutor() {
		EventBusAsyncImpl eventBus = new EventBusAsyncImpl(Executors.newSingleThreadExecutor());
		assertEquals("default", eventBus.getEventBus().identifier());
	}

	@Test
	public void testConstructorExecutorAndEventBusId() {
		EventBusAsyncImpl eventBus = new EventBusAsyncImpl(Executors.newSingleThreadExecutor(), "eventBusId");
		assertEquals("eventBusId", eventBus.getEventBus().identifier());
	}

	@Test
	public void testConstructorExecutorAndExceptionHandler() {
		EventBusAsyncImpl eventBus = new EventBusAsyncImpl(Executors.newSingleThreadExecutor(), new SubscriberExceptionHandler() {

			@Override
			public void handleException(Throwable exception, SubscriberExceptionContext context) {
				throw new RuntimeException(exception);
			}
		});
		assertEquals("default", eventBus.getEventBus().identifier());
	}

	@Test
	public void testPublish() {
		EventBusAsyncImpl eventBus = spy(new EventBusAsyncImpl(Executors.newSingleThreadExecutor()));
		TestEvent event = new TestEvent();

		doReturn(mockEventBus).when(eventBus).getEventBus();
		eventBus.publish(event);
		verify(mockEventBus).post(event);
	}

	@Test
	public void testSubscribe() {
		EventBusAsyncImpl eventBus = spy(new EventBusAsyncImpl(Executors.newSingleThreadExecutor()));
		Object subscriber = new Object();

		doReturn(mockEventBus).when(eventBus).getEventBus();
		eventBus.subscribe(subscriber);
		verify(mockEventBus).register(subscriber);
	}

	@Test
	public void testUnsubscribe() {
		EventBusAsyncImpl eventBus = spy(new EventBusAsyncImpl(Executors.newSingleThreadExecutor()));
		Object subscriber = new Object();

		doReturn(mockEventBus).when(eventBus).getEventBus();
		eventBus.unsubscribe(subscriber);
		verify(mockEventBus).unregister(subscriber);
	}

	static class TestEvent implements Event {
	}

}
