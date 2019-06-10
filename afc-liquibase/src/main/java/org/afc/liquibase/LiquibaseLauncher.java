package org.afc.liquibase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiquibaseLauncher {
	public static void main(String[] args) {
		String[] liquiBaseArgs = new String[args.length + 1];
		System.arraycopy(args, 0, liquiBaseArgs, 0, args.length);
		liquiBaseArgs[args.length] = "--spring.liquibase.contexts=standard";
		SpringApplication.run(LiquibaseLauncher.class, liquiBaseArgs);
	}
}
