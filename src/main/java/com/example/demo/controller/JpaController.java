package com.example.demo.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.GoodDto;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.BlogUser;
import com.example.demo.entity.Customer;
import com.example.demo.entity.GoodInfoBean;
import com.example.demo.entity.Husband;
import com.example.demo.entity.QBlogPost;
import com.example.demo.entity.QBlogUser;
import com.example.demo.entity.QCustomer;
import com.example.demo.entity.QGoodInfoBean;
import com.example.demo.entity.QGoodTypeBean;
import com.example.demo.entity.QUser;
import com.example.demo.entity.QUserBean;
import com.example.demo.entity.User;
import com.example.demo.entity.Wife;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.HusbandRepository;
import com.example.demo.repository.UserRepository;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Controller
public class JpaController {
	// private final Logger logger = Logger.getLogger(JpaController.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private CustomerRepository cr;

	@Autowired
	private UserRepository ur;
	
	@Autowired
	private HusbandRepository hsRepo;

	private JPAQueryFactory queryFactory;

	@PostConstruct
	public void init() {
		queryFactory = new JPAQueryFactory(entityManager);
	}

	@RequestMapping("/getBob")
	@ResponseBody
	public Customer getBob() {
		QCustomer customer = QCustomer.customer;
		Customer bob = queryFactory.selectFrom(customer).where(customer.firstName.eq("Bob")).fetchOne();
		return bob;
	}

	@RequestMapping("/getByName")
	@ResponseBody
	public Customer getCustomerByName(final String name) {
		QCustomer customer = QCustomer.customer;
		Customer bob = queryFactory.selectFrom(customer).where(customer.firstName.eq(name)).fetchOne();
		return bob;
	}

	@RequestMapping("/getAll")
	@ResponseBody
	public List<Customer> getAll() {
		return cr.findAll();
	}

	@RequestMapping("/getUsersInfo")
	@ResponseBody
	public List<UserDto> getUserInfo() {
		QUser user = QUser.user;
		List<UserDto> dtos = queryFactory.select(Projections.bean(UserDto.class, user.id, user.name, user.address))
				.from(user).fetch();
		return dtos;
	}

	@RequestMapping("/getUserById")
	@ResponseBody
	public UserDto getUserById(Integer id) {
		QUser user = QUser.user;
		UserDto dto = queryFactory.select(Projections.bean(UserDto.class, user.id, user.name, user.address)).from(user)
				.where(user.id.eq(id)).fetchOne();

		return dto;
	}

	@RequestMapping("/getOrderAll")
	@ResponseBody
	public List<Customer> getOrderAll() {
		QCustomer customer = QCustomer.customer;
		return queryFactory.selectFrom(customer).orderBy(customer.lastName.asc(), customer.firstName.desc()).fetch();
	}

	@RequestMapping("/getLastName")
	@ResponseBody
	public List<String> getLastName() {
		QCustomer customer = QCustomer.customer;
		return queryFactory.select(customer.lastName).from(customer).groupBy(customer.lastName).fetch();
	}

	@RequestMapping("/getGroupBy")
	@ResponseBody
	public Object getGroupBy() {
		QCustomer customer = QCustomer.customer;
		// return
		// queryFactory.select(customer.id,customer.lastName,customer.count()).from(customer).groupBy(customer.lastName).fetch().toString();

		return queryFactory.selectFrom(customer).select(customer.lastName, customer.count()).groupBy(customer.lastName)
				.fetch().stream().map(tuple -> {
					Map<String, Object> map = new LinkedHashMap<>();
					map.put("lastName", tuple.get(customer.lastName));
					map.put("count", tuple.get(customer.count()));
					return map;

				}).collect(Collectors.toList());

		// return
		// queryFactory.from(customer).groupBy(customer.lastName).select(customer.lastName,customer.lastName.count()).fetch();
		// return
		// queryFactory.from(customer).transform(GroupBy.groupBy(customer.lastName).as(expressions));
		// return null;
	}

	@RequestMapping("/findById")
	@ResponseBody
	public Object getCumtomerById(Long id) {
		return cr.findById(id);
	}

	@RequestMapping("/findName")
	@ResponseBody
	public User findUserByUserName(final String userName) {
		/**
		 * 该例是使用spring data QueryDSL实现
		 */
		QUser quser = QUser.user;
		Predicate predicate = quser.name.eq(userName);// 根据用户名，查询user表
		return ur.findOne(predicate).get();
	}

	/**
	 * Details：单表多条件查询
	 */
	@RequestMapping("/findNameAddr")
	@ResponseBody
	public User findOneByUserNameAndAddress(final String userName, final String address) {
		QUser quser = QUser.user;
		return queryFactory.select(quser).from(quser) // 上面两句代码等价与selectFrom
				.where(quser.name.eq(userName).and(quser.address.eq(address)))// 这句代码等同于where(quser.name.eq(userName),
																				// quser.address.eq(address))
				.fetchOne();
	}

	/**
	 * Details：将查询结果排序
	 */
	@RequestMapping("/findUserAndOrder")
	@ResponseBody
	public List<User> findUserAndOrder() {
		QUser quser = QUser.user;
		return queryFactory.selectFrom(quser).orderBy(quser.id.desc()).fetch();
	}

	/**
	 * Details：分页查询单表
	 */
	public Page<User> findAllAndPager(final int offset, final int pageSize) {
		Predicate predicate = QUser.user.id.lt(10);
		Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
		PageRequest pr = new PageRequest(offset, pageSize, sort);
		return ur.findAll(predicate, pr);
	}

	/**
	 * Details：删除用户
	 */
	public long deleteUser(String userName) {
		QUser quser = QUser.user;
		return queryFactory.delete(quser).where(quser.name.eq(userName)).execute();
	}

	/**
	 * Details：更新记录
	 */
	public long updateUser(final User u, final String userName) {
		QUser quser = QUser.user;
		return queryFactory.update(quser).where(quser.name.eq(userName)).set(quser.name, u.getName())
				.set(quser.age, u.getAge()).set(quser.address, u.getAddress()).execute();
	}

	/**
	 * Details：使用原生Query
	 */
	public User findOneUserByOriginalSql(final String userName) {
		QUser quser = QUser.user;
		Query query = queryFactory.selectFrom(quser).where(quser.name.eq(userName)).createQuery();
		return (User) query.getSingleResult();
	}

	/**
	 * 这种方式联repository
	 * 
	 * @param typeId
	 * @return
	 */
	@RequestMapping(value = "/selectByType")
	@ResponseBody
	public List<GoodInfoBean> selectByType(@RequestParam(value = "typeId") Long typeId // 类型编号
	) {
		// 商品查询实体
		QGoodInfoBean _Q_good = QGoodInfoBean.goodInfoBean;
		// 商品类型查询实体
		QGoodTypeBean _Q_good_type = QGoodTypeBean.goodTypeBean;
		return queryFactory.select(_Q_good).from(_Q_good, _Q_good_type).where(
				// 为两个实体关联查询
				_Q_good.typeId.eq(_Q_good_type.id).and(
						// 查询指定typeid的商品
						_Q_good_type.id.eq(typeId)))
				// 根据排序字段倒序
				.orderBy(_Q_good.order.desc())
				// 执行查询
				.fetch();
	}

	@RequestMapping(value = "/selectWithQueryDSL")
	@ResponseBody
	public List<GoodDto> selectWithQueryDSL() {
		// 商品基本信息
		QGoodInfoBean _Q_good = QGoodInfoBean.goodInfoBean;
		// 商品类型
		QGoodTypeBean _Q_good_type = QGoodTypeBean.goodTypeBean;

		return queryFactory.select(Projections.bean(GoodDto.class, // 返回自定义实体的类型
				_Q_good.id, _Q_good.price, _Q_good.title, _Q_good.unit, _Q_good_type.name.as("typeName"), // 使用别名对应dto内的typeName
				_Q_good_type.id.as("typeId")// 使用别名对应dto内的typeId
		)).from(_Q_good, _Q_good_type)// 构建两表笛卡尔集
				.where(_Q_good.typeId.eq(_Q_good_type.id))// 关联两表
				.orderBy(_Q_good.order.desc())// 倒序
				.fetch();
	}

	/**
	 * 使用java8新特性Collection内stream方法转换dto
	 * 
	 * @return
	 */
	@RequestMapping(value = "/selectWithStream")
	@ResponseBody
	public List<GoodDto> selectWithStream() {
		// 商品基本信息
		QGoodInfoBean _Q_good = QGoodInfoBean.goodInfoBean;
		// 商品类型
		QGoodTypeBean _Q_good_type = QGoodTypeBean.goodTypeBean;
		return queryFactory
				.select(_Q_good.id, _Q_good.price, _Q_good.title, _Q_good.unit, _Q_good_type.name, _Q_good_type.id)
				.from(_Q_good, _Q_good_type)// 构建两表笛卡尔集
				.where(_Q_good.typeId.eq(_Q_good_type.id))// 关联两表
				.orderBy(_Q_good.order.desc())// 倒序
				.fetch().stream()
				// 转换集合内的数据
				.map(tuple -> {
					// 创建商品dto
					GoodDto dto = new GoodDto();
					// 设置商品编号
					dto.setId(tuple.get(_Q_good.id));
					// 设置商品价格
					dto.setPrice(tuple.get(_Q_good.price));
					// 设置商品标题
					dto.setTitle(tuple.get(_Q_good.title));
					// 设置单位
					dto.setUnit(tuple.get(_Q_good.unit));
					// 设置类型编号
					dto.setTypeId(tuple.get(_Q_good_type.id));
					// 设置类型名称
					dto.setTypeName(tuple.get(_Q_good_type.name));
					// 返回本次构建的dto
					return dto;
				})
				// 返回集合并且转换为List<GoodDTO>
				.collect(Collectors.toList());
	}

	@RequestMapping(value = "/countExample")
	@ResponseBody
	public long countExample() {
		// 用户查询实体
		QUserBean _Q_user = QUserBean.userBean;
		return queryFactory.select(_Q_user.id.count())// 根据主键查询总条数
				.from(_Q_user).fetchOne();// 返回总条数
	}
	
	/**
	 * 子查询
	 * @param title
	 * @return
	 */
	@RequestMapping(value = "/getBlogUserByTitle")
	@ResponseBody
	public List<BlogUser> getBlogUserByTitle(String title) {
		QBlogUser user = QBlogUser.blogUser;
		QBlogPost blogPost = QBlogPost.blogPost;
		List<BlogUser> users = queryFactory.selectFrom(user).where(user.id
				.in(JPAExpressions.select(blogPost.user.id).from(blogPost).where(blogPost.title.eq("Hello World!"))))
				.fetch();
		return users;
	}
	
	@RequestMapping(value = "/getBlogUserById")
	@ResponseBody
	public BlogUser getBlogUserById(Long id) {
		QBlogUser user = QBlogUser.blogUser;
		return queryFactory.select(user).from(user).where(user.id.eq(id)).fetchOne();
	}

	@RequestMapping(value = "/addHus")
	@ResponseBody
	public Husband addHusband() {	
		Husband hs = new Husband(null);
		hs.setName("yezuoyi");
		Wife wf = new Wife(null);
		wf.setName("lili");
		hs.setWife(wf);
		wf.setHusband(hs);
		return hsRepo.save(hs);
	}
}
