package com.sbs.java.crud;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.sbs.java.crud.dto.Article;
import com.sbs.java.crud.dto.Member;
import com.sbs.java.crud.util.Util;

public class App {
	private static List<Article> articles;
	private static List<Member> members; {
		
	}
	
	// 프로그램 시작전에 한번 생성하여 데이터를 담아줌
	static {
		articles = new ArrayList<>();
		members = new ArrayList<>();
	}

	public void start() {
		System.out.println("==프로그램 시작==");
		makeTestData();

		Scanner sc = new Scanner(System.in);

		int lastArticleId = 0;
		

//		List<Article> articles = new ArrayList<>(); // article 담을 배열

		while (true) {
			System.out.printf("명령어 ) ");
			String command = sc.nextLine().trim();

			if (command.length() == 0) {
				System.out.println("명령어를 입력해 주세요.");
				continue;
			}
			if (command.equals("system exit")) {
				break;
			}
			
			

			if (command.equals("member join")) {
				int id = members.size() + 1;
				
				String loginId = null;
				String loginPw = null;
				
				String regDate = Util.getNowDateStr();
				while(true) {
					System.out.printf("로그인 아이디: ");
					loginId = sc.nextLine();
					
					if(isJoinableLoginId(loginId) == false) {
						System.out.printf("%s는(은) 이미 사용중인 아이디입니다.\n", loginId);
						continue;
					}
					break;
				}
				
					
//				while(true) {
//				}
				System.out.printf("로그인 비밀번호: ");
				loginPw = sc.nextLine();

				System.out.printf("로그인 이름: ");
				String name = sc.nextLine();

				Member member = new Member(id, regDate, loginId, loginPw, name);
				members.add(member);

				System.out.printf("%d번 회원이 생성 되었습니다.\n", id);
			

			}else if (command.equals("article write")) {
				int add_id;
				System.out.println(articles.size());
				if(articles.size() != 0) {
					add_id = articles.size() +1;
				}else {
					add_id = lastArticleId + 1;					
				}
				lastArticleId = add_id;
				String regDate = Util.getNowDateStr();
				System.out.printf("제목: ");
				String title = sc.nextLine();
				System.out.printf("내용: ");
				String body = sc.nextLine();

				Article article = new Article(add_id, regDate, title, body);
				articles.add(article);

				System.out.printf("%d번 글이 생성 되었습니다.\n", add_id);
			
	
			// 게시판 검색 기능
			} else if (command.startsWith("article list")) {

				if (articles.size() == 0) {
					System.out.println("게시물이 없습니다");
					continue;
				}
				
				String searchKeyword = command.substring("article list".length()).trim();
				List<Article> forListArticles = articles;
				
				if(searchKeyword.length() > 0) {
					forListArticles = new ArrayList<>();
					
					for(Article article : articles) {
						if(article.title.contains(searchKeyword)) {
							forListArticles.add(article);
						}
					}
					if(forListArticles.size() == 0) {
						System.out.println("검색결과가 존재하지 않습니다.");
						continue;
					}
				}
				
				
				System.out.println("번호  /  제목   /   조회");
				for (int i = forListArticles.size() - 1; i >= 0; i--) {
					Article article = forListArticles.get(i);

					System.out.printf("%d     /    %s    /    %d\n", article.id, article.title, article.hit);
				}
				
	
					
			} else if (command.startsWith("article detail ")) {
				String[] commandBits = command.split(" ");
				int id = Integer.parseInt(commandBits[2]);

//				getArticleById 에서 article 객체를 가져옴
				Article foundArticle = getArticleById(id);

		
				if (foundArticle == null) {
					System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
					continue;
				}
				
				foundArticle.increaseHit();

				System.out.printf("번호 : %d\n", foundArticle.id);
				System.out.printf("날짜 : %s\n", foundArticle.regDate);
//				System.out.printf("날짜 : 2021-05-02 16:22:22\n");
				System.out.printf("제목 : %s\n", foundArticle.title);
				System.out.printf("내용 : %s\n", foundArticle.body);
				System.out.printf("조회수 : %d\n", foundArticle.hit);

				
			} else if (command.startsWith("article modify ")) {
				String[] commandBits = command.split(" ");
				int id = Integer.parseInt(commandBits[2]);

				Article foundArticle = getArticleById(id);

//				for (int i = 0; i < articles.size(); i++) {
//					Article article = articles.get(i);
//
//					if (article.id == id) {
//						foundArticle = article;
//						break;
//					}
//				}

				if (foundArticle == null) {
					System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
					continue;
				}

				System.out.printf("새 제목 : ");
				String title = sc.nextLine();
				System.out.println("새 내용 : ");
				String body = sc.nextLine();
				
				
				
				foundArticle.title = title;
				foundArticle.body = body;
				
				System.out.printf("%d번 게시물이 수정되었습니다.\n", id);
				
			} else if (command.startsWith("article delete ")) {
				String[] commandBits = command.split(" ");
				int id = Integer.parseInt(commandBits[2]);

				
				int foundIndex = getArticleIndexById(id);

//				for (int i = 0; i < articles.size(); i++) {
//					Article article = articles.get(i);
//
//					if (article.id == id) {
//						foundIndex = i;
//						break;
//					}
//				}

				if (foundIndex == -1) {
					System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
					continue;
				}

				articles.remove(foundIndex);
				System.out.printf("%d번 게시물이 삭제되었습니다.\n", id);
			
				
				
			} else {
				System.out.printf("%s은(는) 존재하지 않는 명령어 입니다.\n", command);
			}

		}

		sc.close();

		System.out.println("==프로그램 끝==");
	}

	
	private boolean isJoinableLoginId(String loginId) {
		int index = getMemberIndexByLoginId(loginId);
		if(index == -1) {
			return true;
		}
		return false;
	}
	
	private int getMemberIndexByLoginId(String loginId) {
		int i = 0;
		for(Member member : members) {
			if(member.loginId.equals(loginId)) {
				return i;
			}
			i++;
		}
		return -1;
	}


	private int getArticleIndexById(int id) {
				
		int i = 0;
		for(Article article : articles) {
			if(article.id == id) {
				return i;
			}
			i++;
		}
		
		return -1;
	}


	private Article getArticleById(int id) {
		int index = getArticleIndexById(id);
		if(index != -1) {
			return articles.get(index);
		}
		return null;
		
//		for(Article article : articles) {
//			if(article.id == id) {
//				return article;
//			}
//		}
		
//		for(int i = 0; i < articles.size(); i++) {
//			Article article = articles.get(i);
//			
//			if(article.id == id) {
//				return article;
//			}
//		}
	}
	
//	for (int i = 0; i < articles.size(); i++) {
//		Article article = articles.get(i);
//
//		if (article.id == id) {
//			foundArticle = article;
//			break;
//		}
//	}



	private static void makeTestData() {
		System.out.println("테스트를 위한 데이터를 생성합니다.");
		
		articles.add(new Article(1, Util.getNowDateStr(), "제목1", "내용1", 11));
		articles.add(new Article(2, Util.getNowDateStr(), "제목2", "내용2", 22));
		articles.add(new Article(3, Util.getNowDateStr(), "제목3", "내용3", 33));
		 
	}	
	
}
