package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;


/**
 * C，D服务调用B服务，B服务调用A服务，
 * 若A服务不可用则引起B服务不可用，并向雪球似放大到C，D，导致雪崩。
 * 
 * 
 * 防止雪崩效应，必须有一个强大的容错机制。该容错机制需实现以下两点：
 * 1.为网络请求设置超时：
 * 	 必须为网络请求设置超时。正常情况下，一个远程调用一般在几十毫秒内就能得到 响应了。
 * 	 如果依赖的服务不可用或者网络有问题，那么响应时间就会变得很长（几十秒）。
 *   通常情况下，一次远程调用对应着一个线程/进程。如果响应太慢，这个线程/进程就得不到释放。
 *   而线程/进程又对应着系统资源，如果得不到释放的线程/进程越积越多，资源就会逐渐被耗尽，最终导致服务的不可用。
 *   因此，必须为每个网络请求设置超时，让资源尽快释放。
 * 2.使用断路器模式
 * 	 对某个微服务的请求有大量超时（常常说明该微服务不可用），再去让新的请求访问该服务已经没有任何意义，只会无谓消耗资源。
 *   这种代理能够统计一段时间内调用 失败的次数，并决定是正常请求依赖的服务还是直接返回。
 *   断路器可以实现快速失败，如果它在一段时间内检测到许多类似的错误（例如超时）， 
 *   就会在之后的一段时间内，强迫对该服务的调用快速失败，即不再请求所依赖的服务。
 *   这样，应用程序就无须再浪费CPU时间去等待长时间的超时。
 *   断路器也可自动诊断依赖的服务是否已经恢复正常。如果发现依赖的服务已经恢复正常，那么就会恢复请求该服务。
 *   使用这种方式，就可以实现微服务的“自我修复”——当依赖的服务不正常时打开断路器时快速失败，从而防止雪崩效应；
 *   当发 现依赖的服务恢复正常时，又会恢复请求。
 *   （1）正常情况下，断路器关闭，可正常请求依赖的服务。
 *   （2）当一段时间内，请求失败率达到一定阈值（例如错误率达到50%，或100次/分钟 等），
 *   	  断路器就会打开。此时，不会再去请求依赖的服务。
 *   （3）断路器打开一段时间后，会自动进入“半开”状态。此时，断路器可允许一个请求访问依赖的服务。
 *   	  如果该请求能够调用成功，则关闭断路器；否则继续保持打开状态。
 * 
 * 
 * Hystrix是由Netflix开源的一个延迟和容错库，用于隔离访问远程系统、服务或者第三方库，
 * 防止级联失败，从而提升系统的可用性与容错性。Hystrix主要通过以下几点实现 延迟和容错。
 * 	1.包裹请求:使用HystrixCommand (或HystrixObservableCommand)包裹对依赖的调用逻辑，
 * 	  每个命令在独立线程中执行。这使用到了设计模式中的“命令模式”。
 * 	2.跳闸机制：当某服务的错误率超过一定阈值时，Hystrix可以自动或者手动跳闸，停止请求该服务一段时间。
 * 	3.资源隔离：Hystrix为每个依赖都维护了一个小型的线程池(或者信号量)。
 * 	  如果该线程池已满，发往该依赖的请求就被立即拒绝，而不是排队等候，从而加速失败判定。
 * 	4.监控：Hystrix可以近乎实时地监控运行指标和配置的变化9例如成功、失败、超时、以及被拒绝的请求等。
 * 	5.回退机制：当请求失败、超时、被拒绝，或当断路器打开时，执行回退逻辑。回退逻辑可由开发人员自行提供，例如返回一个缺省值。
 * 	6.自我修复：断路器打开一段时间后，会自动进入“半开”状态。断路器打开、关闭、半开的逻辑转换。
 * 
 * 
 * @author cm
 *
 */
@SpringBootApplication
@EnableHystrix
//@EnableCircuitBreaker
public class MicroserviceConsumerMovieRibbonApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceConsumerMovieRibbonApplication.class, args);
	}

}